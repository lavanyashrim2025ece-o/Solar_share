package com.solar.share.repository;

import com.solar.share.entity.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {

    boolean existsByHouseholdIdAndLogDate(Long householdId, LocalDate logDate);

    List<ConsumptionLog> findByHouseholdIdAndLogDateBetweenOrderByLogDateAsc(
            Long householdId, LocalDate start, LocalDate end);

    @Query("select coalesce(sum(c.allocatedShare), 0) from ConsumptionLog c " +
            "where c.generationLog.id = :generationLogId")
    BigDecimal sumAllocatedByGenerationLog(@Param("generationLogId") Long generationLogId);
}