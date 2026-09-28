package com.solar.share.controller;

import com.solar.share.dto.request.ConsumptionLogRequest;
import com.solar.share.dto.response.ConsumptionLogResponse;
import com.solar.share.dto.response.ExportSummaryResponse;
import com.solar.share.dto.response.HouseholdShareResponse;
import com.solar.share.dto.response.MonthlySummaryResponse;
import com.solar.share.service.ConsumptionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/households/{householdId}")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    // Feature 2
    @PostMapping("/consumption")
    public ResponseEntity<ConsumptionLogResponse> logConsumption(@PathVariable Long householdId,
                                                                 @Valid @RequestBody ConsumptionLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consumptionService.logConsumption(householdId, request));
    }

    // Feature 3
    @GetMapping("/share")
    public ResponseEntity<HouseholdShareResponse> getShare(
            @PathVariable Long householdId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(consumptionService.getShare(householdId, date));
    }

    // Feature 4
    @GetMapping("/exports")
    public ResponseEntity<ExportSummaryResponse> getExports(
            @PathVariable Long householdId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(consumptionService.getExports(householdId, month));
    }

    // Feature 5
    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryResponse> getSummary(
            @PathVariable Long householdId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(consumptionService.getMonthlySummary(householdId, month));
    }
}