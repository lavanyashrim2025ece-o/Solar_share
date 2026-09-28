package com.solar.share.controller;

import com.solar.share.dto.request.HouseholdRequest;
import com.solar.share.dto.response.HouseholdResponse;
import com.solar.share.service.HouseholdService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/households")
public class HouseholdController {

    private final HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @PostMapping
    public ResponseEntity<HouseholdResponse> create(@Valid @RequestBody HouseholdRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(householdService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<HouseholdResponse>> getAll() {
        return ResponseEntity.ok(householdService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseholdResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(householdService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HouseholdResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody HouseholdRequest request) {
        return ResponseEntity.ok(householdService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        householdService.delete(id);
        return ResponseEntity.noContent().build();
    }
}