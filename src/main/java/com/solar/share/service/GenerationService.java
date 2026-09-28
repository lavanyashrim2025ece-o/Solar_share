package com.solar.share.service;

import com.solar.share.dto.request.GenerationLogRequest;
import com.solar.share.dto.response.GenerationLogResponse;
import com.solar.share.entity.GenerationLog;
import com.solar.share.entity.Installation;
import com.solar.share.exception.DuplicateResourceException;
import com.solar.share.exception.ResourceNotFoundException;
import com.solar.share.repository.GenerationLogRepository;
import com.solar.share.repository.InstallationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenerationService {

    private final GenerationLogRepository generationLogRepository;
    private final InstallationRepository installationRepository;

    public GenerationService(GenerationLogRepository generationLogRepository,
                             InstallationRepository installationRepository) {
        this.generationLogRepository = generationLogRepository;
        this.installationRepository = installationRepository;
    }

    // Feature 1: log daily total solar units
    @Transactional
    public GenerationLogResponse logGeneration(Long installationId, GenerationLogRequest request) {
        Installation installation = installationRepository.findById(installationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Installation not found with id " + installationId));

        if (generationLogRepository.existsByInstallationIdAndLogDate(installationId, request.logDate())) {
            throw new DuplicateResourceException("Generation already logged for " + request.logDate());
        }

        GenerationLog log = new GenerationLog();
        log.setInstallation(installation);
        log.setLogDate(request.logDate());
        log.setTotalUnitsGenerated(request.totalUnitsGenerated());
        return toResponse(generationLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<GenerationLogResponse> getByInstallation(Long installationId) {
        if (!installationRepository.existsById(installationId)) {
            throw new ResourceNotFoundException("Installation not found with id " + installationId);
        }
        return generationLogRepository.findByInstallationIdOrderByLogDateDesc(installationId)
                .stream().map(this::toResponse).toList();
    }

    private GenerationLogResponse toResponse(GenerationLog g) {
        return new GenerationLogResponse(g.getId(), g.getInstallation().getId(),
                g.getLogDate(), g.getTotalUnitsGenerated());
    }
}