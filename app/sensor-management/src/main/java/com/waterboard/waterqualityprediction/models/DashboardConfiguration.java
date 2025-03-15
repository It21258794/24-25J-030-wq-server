package com.waterboard.waterqualityprediction.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DashboardConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private boolean turbidity;
    private boolean ph;
    private boolean conductivity;
    private boolean turbidityGraph;
    private boolean phGraph;
    private boolean conductivityGraph;
    private boolean chart;
    private boolean totalAnalysis;


    public boolean isTurbidity() {
        return turbidity;
    }

    public void setTurbidity(boolean turbidity) {
        this.turbidity = turbidity;
    }

    public boolean isPh() {
        return ph;
    }

    public void setPh(boolean ph) {
        this.ph = ph;
    }

    public boolean isConductivity() {
        return conductivity;
    }

    public void setConductivity(boolean conductivity) {
        this.conductivity = conductivity;
    }

    public boolean isTurbidityGraph() {
        return turbidityGraph;
    }

    public void setTurbidityGraph(boolean turbidityGraph) {
        this.turbidityGraph = turbidityGraph;
    }

    public boolean isPhGraph() {
        return phGraph;
    }

    public void setPhGraph(boolean phGraph) {
        this.phGraph = phGraph;
    }

    public boolean isConductivityGraph() {
        return conductivityGraph;
    }

    public void setConductivityGraph(boolean conductivityGraph) {
        this.conductivityGraph = conductivityGraph;
    }

    public boolean isChart() {
        return chart;
    }

    public void setChart(boolean chart) {
        this.chart = chart;
    }

    public boolean isTotalAnalysis() {
        return totalAnalysis;
    }

    public void setTotalAnalysis(boolean totalAnalysis) {
        this.totalAnalysis = totalAnalysis;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}