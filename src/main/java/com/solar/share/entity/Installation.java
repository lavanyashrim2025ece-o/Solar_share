package com.solar.share.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "installation")
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capacityKw;

    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Household> households = new ArrayList<>();

    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenerationLog> generationLogs = new ArrayList<>();

    public Installation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public BigDecimal getCapacityKw() { return capacityKw; }
    public void setCapacityKw(BigDecimal capacityKw) { this.capacityKw = capacityKw; }
    public List<Household> getHouseholds() { return households; }
    public void setHouseholds(List<Household> households) { this.households = households; }
    public List<GenerationLog> getGenerationLogs() { return generationLogs; }
    public void setGenerationLogs(List<GenerationLog> generationLogs) { this.generationLogs = generationLogs; }
}