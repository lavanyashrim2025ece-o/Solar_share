package com.solar.share.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InstallationRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Location is required") String location,
        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be greater than 0") BigDecimal capacityKw) {
}