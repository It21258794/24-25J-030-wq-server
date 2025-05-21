package com.waterboard.waterqualityprediction.dto;

public class StepDTO {

    private Long id;
    private String stepName;
    private String stepDescription;
    private Integer stepOrder;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }
    public Integer getStepOrder(){
        return stepOrder;
    }
    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }
}
