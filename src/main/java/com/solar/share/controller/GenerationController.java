package com.solar.share.controller;

import com.solar.share.dto.request.GenerationLogRequest;
import com.solar.share.dto.response.GenerationLogResponse;
import com.solar.share.service.GenerationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/installations/{installationId}/generation")
public class GenerationController {

    private final GenerationService generationService;

    public GenerationController(GenerationService generationService) {
        this.generationService = generationService;
    }

    // Feature 1
    @PostMapping
    public ResponseEntity<GenerationLogResponse> logGeneration(@PathVariable Long installationId,
                                                               @Valid @RequestBody GenerationLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generationService.logGeneration(installationId, request));
    }

    @GetMapping
    public ResponseEntity<List<GenerationLogResponse>> getLogs(@PathVariable Long installationId) {
        return ResponseEntity.ok(generationService.getByInstallation(installationId));
    }
}