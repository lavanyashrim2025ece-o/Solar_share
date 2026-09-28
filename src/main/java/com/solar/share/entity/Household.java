package com.solar.share.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "household")
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "installation_id", nullable = false)
    private Installation installation;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String ownerName;

    // e.g. 0.2500 = 25% of the daily generation
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal allocationRatio;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumptionLog> consumptionLogs = new ArrayList<>();

    public Household() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Installation getInstallation() { return installation; }
    public void setInstallation(Installation installation) { this.installation = installation; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public BigDecimal getAllocationRatio() { return allocationRatio; }
    public void setAllocationRatio(BigDecimal allocationRatio) { this.allocationRatio = allocationRatio; }
    public List<ConsumptionLog> getConsumptionLogs() { return consumptionLogs; }
    public void setConsumptionLogs(List<ConsumptionLog> consumptionLogs) { this.consumptionLogs = consumptionLogs; }
}