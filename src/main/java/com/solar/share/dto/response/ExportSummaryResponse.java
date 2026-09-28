package com.solar.share.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExportSummaryResponse(Long householdId, String month, BigDecimal totalUnitsExported,
                                    List<DailyExport> dailyExports) {

    public record DailyExport(LocalDate date, BigDecimal units) {
    }
}