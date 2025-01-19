package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.StepDTO;
import com.waterboard.waterqualityprediction.models.Step;
import com.waterboard.waterqualityprediction.services.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/steps")
public class StepController {

    @Autowired
    private StepService stepService;

    @PostMapping("/create")
    public Step addStep(@RequestBody StepDTO stepDTO) {
        return stepService.addStep(stepDTO);
    }
}
