package com.solar.share.controller;

import com.solar.share.dto.request.InstallationRequest;
import com.solar.share.dto.response.InstallationResponse;
import com.solar.share.service.InstallationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/installations")
public class InstallationController {

    private final InstallationService installationService;

    public InstallationController(InstallationService installationService) {
        this.installationService = installationService;
    }

    @PostMapping
    public ResponseEntity<InstallationResponse> create(@Valid @RequestBody InstallationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(installationService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<InstallationResponse>> getAll() {
        return ResponseEntity.ok(installationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(installationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstallationResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody InstallationRequest request) {
        return ResponseEntity.ok(installationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        installationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}