package com.solar.share.service;

import com.solar.share.dto.request.HouseholdRequest;
import com.solar.share.dto.response.HouseholdResponse;
import com.solar.share.entity.Household;
import com.solar.share.entity.Installation;
import com.solar.share.exception.BusinessRuleException;
import com.solar.share.exception.ResourceNotFoundException;
import com.solar.share.repository.HouseholdRepository;
import com.solar.share.repository.InstallationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final InstallationRepository installationRepository;

    public HouseholdService(HouseholdRepository householdRepository,
                            InstallationRepository installationRepository) {
        this.householdRepository = householdRepository;
        this.installationRepository = installationRepository;
    }

    @Transactional
    public HouseholdResponse create(HouseholdRequest request) {
        Installation installation = findInstallation(request.installationId());

        BigDecimal used = householdRepository.sumRatiosByInstallation(installation.getId());
        checkRatioLimit(used, request.allocationRatio());

        Household household = new Household();
        household.setInstallation(installation);
        household.setName(request.name());
        household.setOwnerName(request.ownerName());
        household.setAllocationRatio(request.allocationRatio());
        return toResponse(householdRepository.save(household));
    }

    @Transactional(readOnly = true)
    public List<HouseholdResponse> getAll() {
        return householdRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public HouseholdResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public HouseholdResponse update(Long id, HouseholdRequest request) {
        Household household = find(id);
        Installation installation = findInstallation(request.installationId());

        BigDecimal usedByOthers =
                householdRepository.sumRatiosByInstallationExcluding(installation.getId(), id);
        checkRatioLimit(usedByOthers, request.allocationRatio());

        household.setInstallation(installation);
        household.setName(request.name());
        household.setOwnerName(request.ownerName());
        household.setAllocationRatio(request.allocationRatio());
        return toResponse(householdRepository.save(household));
    }

    @Transactional
    public void delete(Long id) {
        householdRepository.delete(find(id));
    }

    // Rule: sum of allocation ratios per installation must not exceed 1.0
    private void checkRatioLimit(BigDecimal alreadyUsed, BigDecimal newRatio) {
        BigDecimal total = alreadyUsed.add(newRatio);
        if (total.compareTo(BigDecimal.ONE) > 0) {
            throw new BusinessRuleException(
                    "Total allocation ratio for this installation would be " + total
                            + ", which exceeds 1.0. Remaining available: "
                            + BigDecimal.ONE.subtract(alreadyUsed));
        }
    }

    private Household find(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Household not found with id " + id));
    }

    private Installation findInstallation(Long id) {
        return installationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Installation not found with id " + id));
    }

    private HouseholdResponse toResponse(Household h) {
        return new HouseholdResponse(h.getId(), h.getInstallation().getId(), h.getName(),
                h.getOwnerName(), h.getAllocationRatio());
    }
}