package com.solar.share.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "generation_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"installation_id", "log_date"}))
public class GenerationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "installation_id", nullable = false)
    private Installation installation;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalUnitsGenerated;

    @OneToMany(mappedBy = "generationLog", cascade = CascadeType.REMOVE)
    private List<ConsumptionLog> consumptionLogs = new ArrayList<>();

    public GenerationLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Installation getInstallation() { return installation; }
    public void setInstallation(Installation installation) { this.installation = installation; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public BigDecimal getTotalUnitsGenerated() { return totalUnitsGenerated; }
    public void setTotalUnitsGenerated(BigDecimal totalUnitsGenerated) { this.totalUnitsGenerated = totalUnitsGenerated; }
    public List<ConsumptionLog> getConsumptionLogs() { return consumptionLogs; }
    public void setConsumptionLogs(List<ConsumptionLog> consumptionLogs) { this.consumptionLogs = consumptionLogs; }
}