package com.solar.share.service;

import com.solar.share.dto.request.InstallationRequest;
import com.solar.share.dto.response.InstallationResponse;
import com.solar.share.entity.Installation;
import com.solar.share.exception.ResourceNotFoundException;
import com.solar.share.repository.InstallationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstallationService {

    private final InstallationRepository installationRepository;

    public InstallationService(InstallationRepository installationRepository) {
        this.installationRepository = installationRepository;
    }

    @Transactional
    public InstallationResponse create(InstallationRequest request) {
        Installation installation = new Installation();
        apply(installation, request);
        return toResponse(installationRepository.save(installation));
    }

    @Transactional(readOnly = true)
    public List<InstallationResponse> getAll() {
        return installationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public InstallationResponse getById(Long id) {
        return toResponse(find(id));
    }

    @Transactional
    public InstallationResponse update(Long id, InstallationRequest request) {
        Installation installation = find(id);
        apply(installation, request);
        return toResponse(installationRepository.save(installation));
    }

    @Transactional
    public void delete(Long id) {
        installationRepository.delete(find(id));
    }

    private Installation find(Long id) {
        return installationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Installation not found with id " + id));
    }

    private void apply(Installation installation, InstallationRequest request) {
        installation.setName(request.name());
        installation.setLocation(request.location());
        installation.setCapacityKw(request.capacityKw());
    }

    private InstallationResponse toResponse(Installation i) {
        return new InstallationResponse(i.getId(), i.getName(), i.getLocation(), i.getCapacityKw());
    }
}