package com.solar.share.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HouseholdShareResponse(Long householdId, LocalDate date, BigDecimal totalGenerated,
                                     BigDecimal allocationRatio, BigDecimal allocatedShare) {
}