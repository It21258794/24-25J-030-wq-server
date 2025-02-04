package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.StepValueDTO;
import com.waterboard.waterqualityprediction.models.StepValue;
import com.waterboard.waterqualityprediction.services.StepValueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/step-values")
public class StepValueController {

    private final StepValueService stepValueService;

    public StepValueController(StepValueService stepValueService) {
        this.stepValueService = stepValueService;
    }

    @PostMapping("/create")
    public ResponseEntity<StepValueDTO> createStepValue(@RequestBody StepValueDTO stepValueDTO) {
        StepValue createdStepValue = stepValueService.createStepValue(stepValueDTO);
        // Return the StepValueDTO with testName and chemicalName
        return ResponseEntity.status(HttpStatus.CREATED).body(stepValueDTO);
    }

    // Endpoint to remove test or chemical, and delete the record if both are removed
    @PutMapping("/remove-test-or-chemical/{id}")
    public ResponseEntity<StepValue> updateStepValue(@PathVariable Long id, @RequestBody StepValueDTO stepValueDTO) {
        StepValue updatedStepValue = stepValueService.updateStepValue(id, stepValueDTO);
        if (updatedStepValue != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedStepValue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint to get data by stepId only
    @GetMapping("/get-by-step/{stepId}")
    public ResponseEntity<List<StepValueDTO>> getStepValues(@PathVariable Long stepId) {
        List<StepValueDTO> stepValueDTOs = stepValueService.getStepValuesByStepId(stepId);
        if (!stepValueDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(stepValueDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }


    //add values to test and chemicals.
    @PutMapping("/update-test-or-chemical-values/{id}/{stepId}")
    public ResponseEntity<StepValueDTO> updateStepValue(@PathVariable Long id,
                                                        @PathVariable Long stepId,
                                                        @RequestBody StepValueDTO stepValueDTO) {
        StepValue updatedStepValue = stepValueService.updateStepValue(id, stepId, stepValueDTO);
        if (updatedStepValue != null) {
            StepValueDTO responseDTO = new StepValueDTO();
            responseDTO.setStepId(updatedStepValue.getStepId());
            responseDTO.setTestId(updatedStepValue.getTestId());
            responseDTO.setChemicalId(updatedStepValue.getChemicalId());
            responseDTO.setTestValue(updatedStepValue.getTestValue());
            responseDTO.setChemicalValue(updatedStepValue.getChemicalValue());
            responseDTO.setStatus(updatedStepValue.getStatus());
            responseDTO.setValueAddedDate(updatedStepValue.getValueAddedDate());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //get all tests
    @GetMapping("/get-all-step-values")
    public ResponseEntity<List<StepValueDTO>> getAllStepValues() {
        List<StepValueDTO> stepValueDTOs = stepValueService.getAllStepValues();
        if (stepValueDTOs != null && !stepValueDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(stepValueDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

}
