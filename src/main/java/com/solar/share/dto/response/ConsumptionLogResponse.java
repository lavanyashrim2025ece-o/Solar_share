package com.solar.share.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumptionLogResponse(Long id, Long householdId, LocalDate logDate,
                                     BigDecimal unitsConsumed, BigDecimal allocatedShare,
                                     BigDecimal unitsExported) {
}