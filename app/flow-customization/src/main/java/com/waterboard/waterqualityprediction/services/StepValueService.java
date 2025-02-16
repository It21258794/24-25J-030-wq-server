package com.waterboard.waterqualityprediction.services;


import com.waterboard.waterqualityprediction.dto.StepValueDTO;
import com.waterboard.waterqualityprediction.models.StepValue;
import com.waterboard.waterqualityprediction.models.Test;
import com.waterboard.waterqualityprediction.models.Chemical;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StepValueService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public StepValue createStepValue(StepValueDTO stepValueDTO) {
        // Create a new StepValue entity
        StepValue stepValue = new StepValue();
        stepValue.setStepId(stepValueDTO.getStepId());
        stepValue.setTestId(stepValueDTO.getTestId());
        stepValue.setChemicalId(stepValueDTO.getChemicalId());

        // Set default status if not set
        stepValue.setStatus(stepValueDTO.getStatus() != null ? stepValueDTO.getStatus() : "Pending");
        // Fetch test and chemical names based on their IDs
        if (stepValueDTO.getTestId() != null) {
            Test test = entityManager.find(Test.class, stepValueDTO.getTestId());
            if (test != null) {
                stepValueDTO.setTestName(test.getTestName());  // Assuming Test entity has getName()
            }
        }

        if (stepValueDTO.getChemicalId() != null) {
            Chemical chemical = entityManager.find(Chemical.class, stepValueDTO.getChemicalId());
            if (chemical != null) {
                stepValueDTO.setChemicalName(chemical.getChemicalName());  // Assuming Chemical entity has getName()
            }
        }

        // Persist the entity in the database
        entityManager.persist(stepValue);

        // Return the persisted StepValue object with the names populated
        return stepValue;
    }

//    //function to delete test or chemical
//    @Transactional
//    public StepValue updateStepValue(Long id, StepValueDTO stepValueDTO) {
//        // Find the existing StepValue entity by id
//        StepValue stepValue = entityManager.find(StepValue.class, id);
//        if (stepValue != null) {
//            // Update fields if provided
//            if (stepValueDTO.getTestId() != null) {
//                stepValue.setTestId(stepValueDTO.getTestId());
//            } else {
//                stepValue.setTestId(null); // Set to null if test is removed
//            }
//
//            if (stepValueDTO.getChemicalId() != null) {
//                stepValue.setChemicalId(stepValueDTO.getChemicalId());
//            } else {
//                stepValue.setChemicalId(null); // Set to null if chemical is removed
//            }
//
//            // If both testId and chemicalId are null, delete the record
//            if (stepValue.getTestId() == null && stepValue.getChemicalId() == null) {
//                entityManager.remove(stepValue);
//                return null; // Return null to indicate record is deleted
//            }
//
//            // Persist the updated entity in the database
//            entityManager.merge(stepValue);
//
//            // Return the updated StepValue object
//            return stepValue;
//        }
//        return null; // Return null if the record is not found
//    }


        @Transactional
        public List<StepValueDTO> getStepValuesByStepId(Long stepId) {
            // Find StepValue entities by stepId
            String sql = "SELECT s FROM StepValue s WHERE s.stepId = :stepId AND s.status = 'Pending'";
            Query query = entityManager.createQuery(sql);
            query.setParameter("stepId", stepId);

            List<StepValue> stepValues = query.getResultList();
            List<StepValueDTO> stepValueDTOs = new ArrayList<>();

            for (StepValue stepValue : stepValues) {
                StepValueDTO stepValueDTO = new StepValueDTO();
                stepValueDTO.setStepId(stepValue.getStepId());
                stepValueDTO.setTestId(stepValue.getTestId());
                stepValueDTO.setChemicalId(stepValue.getChemicalId());
                stepValueDTO.setStatus(stepValue.getStatus());
                stepValueDTO.setTestValue(stepValue.getTestValue());
                stepValueDTO.setChemicalValue(stepValue.getChemicalValue());
                stepValueDTO.setValueAddedDate(stepValue.getValueAddedDate());

                // Fetch test and chemical names based on their IDs
                if (stepValue.getTestId() != null) {
                    Test test = entityManager.find(Test.class, stepValue.getTestId());
                    if (test != null) {
                        stepValueDTO.setTestName(test.getTestName());
                    } else {
                        System.out.println("Test not found for ID: " + stepValue.getTestId());
                    }
                }

                if (stepValue.getChemicalId() != null) {
                    Chemical chemical = entityManager.find(Chemical.class, stepValue.getChemicalId());
                    if (chemical != null) {
                        stepValueDTO.setChemicalName(chemical.getChemicalName());
                    } else {
                        System.out.println("Chemical not found for ID: " + stepValue.getChemicalId());
                    }
                }

                stepValueDTOs.add(stepValueDTO);
            }

            return stepValueDTOs;
        }

    @Transactional
    public StepValue updateStepValue(Long id, StepValueDTO stepValueDTO) {
        // Find the existing StepValue entity by id
        String sql = "SELECT s FROM StepValue s WHERE s.id = :id";
        Query query = entityManager.createQuery(sql);
        query.setParameter("id", id);

        try {
            StepValue stepValue = (StepValue) query.getSingleResult();
            if (stepValue != null) {
                // Update the test and chemical values
                if (stepValueDTO.getTestValue() != null) {
                    stepValue.setTestValue(stepValueDTO.getTestValue());
                }

                if (stepValueDTO.getChemicalValue() != null) {
                    stepValue.setChemicalValue(stepValueDTO.getChemicalValue());
                }

                // Update status to "Confirmed"
                stepValue.setStatus("Confirmed");

                // Set value added date
                stepValue.setValueAddedDate(LocalDateTime.now());

                // Persist the updated entity
                entityManager.merge(stepValue);

                // Return the updated StepValue object
                return stepValue;
            }
        } catch (NoResultException e) {
            // Handle the case where no entity is found
            return null;
        }

        return null; // Return null if the record is not found
    }
    //get all tests
    @Transactional
    public List<StepValueDTO> getAllStepValues() {
        // Fetch all StepValue entities from the database
        String sql = "SELECT s FROM StepValue s WHERE s.status = 'Confirmed'";
        Query query = entityManager.createQuery(sql);
        List<StepValue> stepValues = query.getResultList();

        List<StepValueDTO> stepValueDTOs = new ArrayList<>();

        for (StepValue stepValue : stepValues) {
            StepValueDTO stepValueDTO = new StepValueDTO();
            stepValueDTO.setStepId(stepValue.getStepId());
            stepValueDTO.setTestId(stepValue.getTestId());
            stepValueDTO.setChemicalId(stepValue.getChemicalId());
            stepValueDTO.setStatus(stepValue.getStatus());

            // Fetch test name based on testId
            if (stepValue.getTestId() != null) {
                Test test = entityManager.find(Test.class, stepValue.getTestId());
                if (test != null) {
                    stepValueDTO.setTestName(test.getTestName());
                }
            }

            // Fetch chemical name based on chemicalId
            if (stepValue.getChemicalId() != null) {
                Chemical chemical = entityManager.find(Chemical.class, stepValue.getChemicalId());
                if (chemical != null) {
                    stepValueDTO.setChemicalName(chemical.getChemicalName());
                }
            }

            // Add the populated StepValueDTO to the list
            stepValueDTOs.add(stepValueDTO);
        }

        return stepValueDTOs;
    }

}
