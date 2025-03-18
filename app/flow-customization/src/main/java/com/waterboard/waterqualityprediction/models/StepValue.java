package com.waterboard.waterqualityprediction.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "step_values")
public class StepValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_id", nullable = false)
    private Long stepId;

    @Column(name = "test_id")
    private Long testId;

    @Column(name = "chemical_id")
    private Long chemicalId;

    @Column(name = "test_value")
    private String testValue;

    @Column(name = "chemical_value")
    private String chemicalValue;

    @Column(name = "value_added_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime valueAddedDate;  // new field for storing the date and time

    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'Pending'")
    private String status;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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