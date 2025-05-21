package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.dto.WaterQualityRequest;
import com.waterboard.waterqualityprediction.models.WaterQuality;
import com.waterboard.waterqualityprediction.services.WaterTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/water-treatment")
public class WaterTreatmentController {

    private final WaterTreatmentService waterTreatmentService;

    @Autowired
    public WaterTreatmentController(WaterTreatmentService waterTreatmentService) {
        this.waterTreatmentService = waterTreatmentService;
    }

    @PostMapping("/predict")
    public WaterQuality predictTreatedWater(@RequestBody WaterQualityRequest request) {
        return waterTreatmentService.predictTreatedWater(request);
    }
}