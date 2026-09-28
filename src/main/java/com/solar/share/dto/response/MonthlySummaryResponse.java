package com.solar.share.dto.response;

import java.math.BigDecimal;

// netUsage = totalConsumed - totalAllocated (negative = net exporter)
public record MonthlySummaryResponse(Long householdId, String householdName, String month,
                                     BigDecimal totalAllocated, BigDecimal totalConsumed,
                                     BigDecimal totalExported, BigDecimal netUsage) {
}