package com.solar.share.dto.response;

import java.math.BigDecimal;

public record HouseholdResponse(Long id, Long installationId, String name,
                                String ownerName, BigDecimal allocationRatio) {
}