package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.dto.StepDTO;
import com.waterboard.waterqualityprediction.models.Step;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StepService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Step addStep(StepDTO stepDTO) {
        Step step = new Step();
        step.setStepName(stepDTO.getStepName());
        step.setStepDescription(stepDTO.getStepDescription());

        entityManager.persist(step); // Persisting the step manually
        return step;
    }
}
