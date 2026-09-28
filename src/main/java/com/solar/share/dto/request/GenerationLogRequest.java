package com.solar.share.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GenerationLogRequest(
        @NotNull(message = "Log date is required")
        @PastOrPresent(message = "Log date cannot be in the future") LocalDate logDate,
        @NotNull(message = "Total units generated is required")
        @Positive(message = "Total units generated must be greater than 0") BigDecimal totalUnitsGenerated) {
}