package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.dto.StepDTO;
import com.waterboard.waterqualityprediction.models.Step;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StepService {

    @PersistenceContext
    private EntityManager entityManager;

    //function to add new step
    @Transactional
    public Step addStep(StepDTO stepDTO) {
        Step step = new Step();
        step.setStepName(stepDTO.getStepName());
        step.setStepDescription(stepDTO.getStepDescription());
        step.setStepOrder(stepDTO.getStepOrder());

        // Persist the step manually
        entityManager.persist(step);
        return step;
    }

    //function to fetch all steps
    public List<Step> getAllSteps() {
        try {
            // Ensure that you use the correct JPQL query
            TypedQuery<Step> query = entityManager.createQuery("SELECT s FROM Step s ORDER BY s.stepOrder ASC", Step.class);
            List<Step> steps = query.getResultList();
            return steps;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public String updateStepOrder(List<StepDTO> stepsDTO) {
        for (StepDTO stepDTO : stepsDTO) {
            Step step = entityManager.find(Step.class, stepDTO.getId());  // Find by ID
            if (step != null) {
                step.setStepOrder(stepDTO.getStepOrder());  // Only updating the order field
            } else {
                // Optionally log or throw an error if the step is not found
                System.out.println("Step with ID " + stepDTO.getId() + " not found.");
            }
        }
        return "Steps order updated successfully.";
    }

}
