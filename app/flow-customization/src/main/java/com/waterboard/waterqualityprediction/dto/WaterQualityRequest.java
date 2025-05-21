package com.waterboard.waterqualityprediction.dto;

public class WaterQualityRequest {
    private double raw_turbidity; // Match the Flask API's expected field name
    private double raw_ph;       // Match the Flask API's expected field name
    private double raw_conductivity; // Match the Flask API's expected field name

    // Getters and Setters
    public double getRaw_turbidity() {
        return raw_turbidity;
    }

    public void setRaw_turbidity(double raw_turbidity) {
        this.raw_turbidity = raw_turbidity;
    }

    public double getRaw_ph() {
        return raw_ph;
    }

    public void setRaw_ph(double raw_ph) {
        this.raw_ph = raw_ph;
    }

    public double getRaw_conductivity() {
        return raw_conductivity;
    }

    public void setRaw_conductivity(double raw_conductivity) {
        this.raw_conductivity = raw_conductivity;
    }
}