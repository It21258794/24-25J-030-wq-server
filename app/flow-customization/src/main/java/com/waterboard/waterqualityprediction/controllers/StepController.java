package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.StepDTO;
import com.waterboard.waterqualityprediction.models.Step;
import com.waterboard.waterqualityprediction.services.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/steps")
public class StepController {

    @Autowired
    private StepService stepService;

    //function to add new step
    @PostMapping("/create")
    public Step addStep(@RequestBody StepDTO stepDTO) {
//        System.out.println("Received StepDTO: " + stepDTO);
        return stepService.addStep(stepDTO);
    }

    //function to fetch all steps
    @GetMapping ("/get/all-steps")
    public List<Step> getAllSteps() {
        return stepService.getAllSteps();
    }

    @PutMapping("/update/step-order")
    public String updateStepOrder(@RequestBody List<StepDTO> stepsDTO) {
        return stepService.updateStepOrder(stepsDTO);
    }

}
