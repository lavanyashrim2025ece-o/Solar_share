package com.solar.share.repository;

import com.solar.share.entity.GenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GenerationLogRepository extends JpaRepository<GenerationLog, Long> {

    boolean existsByInstallationIdAndLogDate(Long installationId, LocalDate logDate);

    Optional<GenerationLog> findByInstallationIdAndLogDate(Long installationId, LocalDate logDate);

    List<GenerationLog> findByInstallationIdOrderByLogDateDesc(Long installationId);
}