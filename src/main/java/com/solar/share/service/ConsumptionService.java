package com.solar.share.service;

import com.solar.share.dto.request.ConsumptionLogRequest;
import com.solar.share.dto.response.ConsumptionLogResponse;
import com.solar.share.dto.response.ExportSummaryResponse;
import com.solar.share.dto.response.HouseholdShareResponse;
import com.solar.share.dto.response.MonthlySummaryResponse;
import com.solar.share.entity.ConsumptionLog;
import com.solar.share.entity.GenerationLog;
import com.solar.share.entity.Household;
import com.solar.share.exception.BusinessRuleException;
import com.solar.share.exception.DuplicateResourceException;
import com.solar.share.exception.ResourceNotFoundException;
import com.solar.share.repository.ConsumptionLogRepository;
import com.solar.share.repository.GenerationLogRepository;
import com.solar.share.repository.HouseholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ConsumptionService {

    private final ConsumptionLogRepository consumptionLogRepository;
    private final GenerationLogRepository generationLogRepository;
    private final HouseholdRepository householdRepository;

    public ConsumptionService(ConsumptionLogRepository consumptionLogRepository,
                              GenerationLogRepository generationLogRepository,
                              HouseholdRepository householdRepository) {
        this.consumptionLogRepository = consumptionLogRepository;
        this.generationLogRepository = generationLogRepository;
        this.householdRepository = householdRepository;
    }

    // Feature 2 + 4: log consumption, calculate share and export
    @Transactional
    public ConsumptionLogResponse logConsumption(Long householdId, ConsumptionLogRequest request) {
        Household household = findHousehold(householdId);
        Long installationId = household.getInstallation().getId();

        if (consumptionLogRepository.existsByHouseholdIdAndLogDate(householdId, request.logDate())) {
            throw new DuplicateResourceException("Consumption already logged for household "
                    + householdId + " on " + request.logDate());
        }

        GenerationLog generation = generationLogRepository
                .findByInstallationIdAndLogDate(installationId, request.logDate())
                .orElseThrow(() -> new BusinessRuleException("No solar generation logged for "
                        + request.logDate() + ". Log generation before consumption."));

        BigDecimal share = calculateShare(generation.getTotalUnitsGenerated(),
                household.getAllocationRatio());

        // Rule 1: total allocated shares must never exceed the day's generation
        BigDecimal alreadyAllocated =
                consumptionLogRepository.sumAllocatedByGenerationLog(generation.getId());
        if (alreadyAllocated.add(share).compareTo(generation.getTotalUnitsGenerated()) > 0) {
            throw new BusinessRuleException("Total allocated shares would exceed the "
                    + generation.getTotalUnitsGenerated() + " units generated on " + request.logDate());
        }

        // Rule 2: exported = share - consumption, floored at zero
        BigDecimal exported = calculateExport(share, request.unitsConsumed());

        ConsumptionLog log = new ConsumptionLog();
        log.setHousehold(household);
        log.setGenerationLog(generation);
        log.setLogDate(request.logDate());
        log.setUnitsConsumed(request.unitsConsumed());
        log.setAllocatedShare(share);
        log.setUnitsExported(exported);

        ConsumptionLog saved = consumptionLogRepository.save(log);
        System.out.println("[NOTIFICATION] Household " + householdId + " logged "
                + saved.getUnitsConsumed() + " units on " + saved.getLogDate()
                + ", exported " + saved.getUnitsExported());
        return toResponse(saved);
    }

    // Feature 3: household share for a given day
    @Transactional(readOnly = true)
    public HouseholdShareResponse getShare(Long householdId, LocalDate date) {
        Household household = findHousehold(householdId);

        GenerationLog generation = generationLogRepository
                .findByInstallationIdAndLogDate(household.getInstallation().getId(), date)
                .orElseThrow(() -> new ResourceNotFoundException("No solar generation logged for " + date));

        return new HouseholdShareResponse(householdId, date,
                generation.getTotalUnitsGenerated(),
                household.getAllocationRatio(),
                calculateShare(generation.getTotalUnitsGenerated(), household.getAllocationRatio()));
    }

    // Feature 4: units exported back to the grid in a month
    @Transactional(readOnly = true)
    public ExportSummaryResponse getExports(Long householdId, YearMonth month) {
        findHousehold(householdId);
        List<ConsumptionLog> logs = monthLogs(householdId, month);

        List<ExportSummaryResponse.DailyExport> daily = logs.stream()
                .map(l -> new ExportSummaryResponse.DailyExport(l.getLogDate(), l.getUnitsExported()))
                .toList();

        BigDecimal total = logs.stream()
                .map(ConsumptionLog::getUnitsExported)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExportSummaryResponse(householdId, month.toString(), total, daily);
    }

    // Feature 5: monthly net usage summary
    @Transactional(readOnly = true)
    public MonthlySummaryResponse getMonthlySummary(Long householdId, YearMonth month) {
        Household household = findHousehold(householdId);
        List<ConsumptionLog> logs = monthLogs(householdId, month);

        BigDecimal allocated = logs.stream().map(ConsumptionLog::getAllocatedShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal consumed = logs.stream().map(ConsumptionLog::getUnitsConsumed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal exported = logs.stream().map(ConsumptionLog::getUnitsExported)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlySummaryResponse(householdId, household.getName(), month.toString(),
                allocated, consumed, exported, consumed.subtract(allocated));
    }

    private BigDecimal calculateShare(BigDecimal totalGenerated, BigDecimal ratio) {
        // RoundingMode.DOWN so rounding never pushes shares above the total generated
        return totalGenerated.multiply(ratio).setScale(2, RoundingMode.DOWN);
    }

    private BigDecimal calculateExport(BigDecimal share, BigDecimal consumed) {
        return share.subtract(consumed).max(BigDecimal.ZERO);
    }

    private List<ConsumptionLog> monthLogs(Long householdId, YearMonth month) {
        return consumptionLogRepository.findByHouseholdIdAndLogDateBetweenOrderByLogDateAsc(
                householdId, month.atDay(1), month.atEndOfMonth());
    }

    private Household findHousehold(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found with id " + id));
    }

    private ConsumptionLogResponse toResponse(ConsumptionLog c) {
        return new ConsumptionLogResponse(c.getId(), c.getHousehold().getId(), c.getLogDate(),
                c.getUnitsConsumed(), c.getAllocatedShare(), c.getUnitsExported());
    }
}