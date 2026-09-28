package com.solar.share.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GenerationLogResponse(Long id, Long installationId, LocalDate logDate,
                                    BigDecimal totalUnitsGenerated) {
}