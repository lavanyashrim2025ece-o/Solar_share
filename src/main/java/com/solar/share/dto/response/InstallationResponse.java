package com.solar.share.dto.response;

import java.math.BigDecimal;

public record InstallationResponse(Long id, String name, String location, BigDecimal capacityKw) {
}