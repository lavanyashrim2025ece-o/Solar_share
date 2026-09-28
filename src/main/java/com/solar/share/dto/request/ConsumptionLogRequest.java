package com.solar.share.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumptionLogRequest(
        @NotNull(message = "Log date is required")
        @PastOrPresent(message = "Log date cannot be in the future") LocalDate logDate,
        @NotNull(message = "Units consumed is required")
        @PositiveOrZero(message = "Units consumed cannot be negative") BigDecimal unitsConsumed) {
}