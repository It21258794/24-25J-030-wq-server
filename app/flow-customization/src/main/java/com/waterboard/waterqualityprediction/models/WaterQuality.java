package com.waterboard.waterqualityprediction.models;

public class WaterQuality {
    private double treated_turbidity; // Match the Flask API's response field name
    private double treated_ph;       // Match the Flask API's response field name
    private double treated_conductivity; // Match the Flask API's response field name

    // Getters and Setters
    public double getTreated_turbidity() {
        return treated_turbidity;
    }

    public void setTreated_turbidity(double treated_turbidity) {
        this.treated_turbidity = treated_turbidity;
    }

    public double getTreated_ph() {
        return treated_ph;
    }

    public void setTreated_ph(double treated_ph) {
        this.treated_ph = treated_ph;
    }

    public double getTreated_conductivity() {
        return treated_conductivity;
    }

    public void setTreated_conductivity(double treated_conductivity) {
        this.treated_conductivity = treated_conductivity;
    }
}