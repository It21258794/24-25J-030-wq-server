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
    private boolean totalTreatedAnalysis;
    private boolean treatedChart;
    private boolean limeUsageChart;
    private boolean pacChart;
    private boolean chlorineUsageChart;
    private boolean limeUsage;
    private boolean pacUsage;
    private boolean chlorineUsage;

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

    public boolean isTotalTreatedAnalysis() {
        return totalTreatedAnalysis;
    }

    public void setTotalTreatedAnalysis(boolean totalTreatedAnalysis) {
        this.totalTreatedAnalysis = totalTreatedAnalysis;
    }

    public boolean isTreatedChart() {
        return treatedChart;
    }

    public void setTreatedChart(boolean treatedChart) {
        this.treatedChart = treatedChart;
    }

    public boolean isLimeUsageChart() {
        return limeUsageChart;
    }

    public void setLimeUsageChart(boolean limeUsageChart) {
        this.limeUsageChart = limeUsageChart;
    }

    public boolean isPacChart() {
        return pacChart;
    }

    public void setPacChart(boolean pacChart) {
        this.pacChart = pacChart;
    }

    public boolean isChlorineUsageChart() {
        return chlorineUsageChart;
    }

    public void setChlorineUsageChart(boolean chlorineUsageChart) {
        this.chlorineUsageChart = chlorineUsageChart;
    }

    public boolean isLimeUsage() {
        return limeUsage;
    }

    public void setLimeUsage(boolean limeUsage) {
        this.limeUsage = limeUsage;
    }

    public boolean isPacUsage() {
        return pacUsage;
    }

    public void setPacUsage(boolean pacUsage) {
        this.pacUsage = pacUsage;
    }

    public boolean isChlorineUsage() {
        return chlorineUsage;
    }

    public void setChlorineUsage(boolean chlorineUsage) {
        this.chlorineUsage = chlorineUsage;
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
