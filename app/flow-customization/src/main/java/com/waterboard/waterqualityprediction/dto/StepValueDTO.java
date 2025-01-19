package com.waterboard.waterqualityprediction.dto;

import java.time.LocalDateTime;

public class StepValueDTO {

    private Long stepId;
    private Long testId;
    private Long chemicalId;
    private String testName;
    private String chemicalName;
    private String testValue;
    private String chemicalValue;
    private LocalDateTime valueAddedDate;
    private String status;

    // Getters and Setters
    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(Long chemicalId) {
        this.chemicalId = chemicalId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    public String getChemicalValue() {
        return chemicalValue;
    }

    public void setChemicalValue(String chemicalValue) {
        this.chemicalValue = chemicalValue;
    }

    public LocalDateTime getValueAddedDate() {
        return valueAddedDate;
    }

    public void setValueAddedDate(LocalDateTime valueAddedDate) {
        this.valueAddedDate = valueAddedDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}