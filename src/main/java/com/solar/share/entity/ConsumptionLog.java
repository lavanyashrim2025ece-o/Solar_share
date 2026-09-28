package com.solar.share.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "consumption_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"household_id", "log_date"}))
public class ConsumptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "generation_log_id", nullable = false)
    private GenerationLog generationLog;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitsConsumed;

    // calculated by the service: totalUnitsGenerated x allocationRatio
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedShare;

    // calculated by the service: max(0, allocatedShare - unitsConsumed)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitsExported;

    public ConsumptionLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Household getHousehold() { return household; }
    public void setHousehold(Household household) { this.household = household; }
    public GenerationLog getGenerationLog() { return generationLog; }
    public void setGenerationLog(GenerationLog generationLog) { this.generationLog = generationLog; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public BigDecimal getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(BigDecimal unitsConsumed) { this.unitsConsumed = unitsConsumed; }
    public BigDecimal getAllocatedShare() { return allocatedShare; }
    public void setAllocatedShare(BigDecimal allocatedShare) { this.allocatedShare = allocatedShare; }
    public BigDecimal getUnitsExported() { return unitsExported; }
    public void setUnitsExported(BigDecimal unitsExported) { this.unitsExported = unitsExported; }
}