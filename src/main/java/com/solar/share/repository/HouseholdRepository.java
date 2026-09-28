package com.solar.share.repository;

import com.solar.share.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface HouseholdRepository extends JpaRepository<Household, Long> {

    @Query("select coalesce(sum(h.allocationRatio), 0) from Household h " +
            "where h.installation.id = :installationId")
    BigDecimal sumRatiosByInstallation(@Param("installationId") Long installationId);

    @Query("select coalesce(sum(h.allocationRatio), 0) from Household h " +
            "where h.installation.id = :installationId and h.id <> :excludeId")
    BigDecimal sumRatiosByInstallationExcluding(@Param("installationId") Long installationId,
                                                @Param("excludeId") Long excludeId);
}