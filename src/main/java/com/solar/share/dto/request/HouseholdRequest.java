package com.solar.share.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record HouseholdRequest(
        @NotNull(message = "Installation id is required") Long installationId,
        @NotBlank(message = "Household name is required") String name,
        @NotBlank(message = "Owner name is required") String ownerName,
        @NotNull(message = "Allocation ratio is required")
        @DecimalMin(value = "0.0001", message = "Allocation ratio must be greater than 0")
        @DecimalMax(value = "1.0", message = "Allocation ratio cannot exceed 1.0")
        BigDecimal allocationRatio) {
}