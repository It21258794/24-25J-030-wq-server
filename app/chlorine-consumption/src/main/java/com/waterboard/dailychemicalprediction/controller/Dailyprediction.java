package com.waterboard.dailychemicalprediction.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.waterboard.dailychemicalprediction.service.DailypredictionService;

@RestController
@CrossOrigin
public class Dailyprediction {

    @Autowired
    private DailypredictionService pService;

    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predict(@RequestBody Map<String, Float> inputData) {
        // Validate the input data
        if (inputData == null || inputData.isEmpty()) {
            return new ResponseEntity<>(createErrorResponse("Input data cannot be empty"), HttpStatus.BAD_REQUEST);
        }

        float waterVolume = inputData.getOrDefault("water_volume", -1f);
        float initialChlorine = inputData.getOrDefault("initial_cholrine", -1f);
        float initialPh = inputData.getOrDefault("initial_ph", -1f);
        float initialTurbidity = inputData.getOrDefault("initial_turbidity", -1f);

        // Validate each input
        if (waterVolume <= 0) {
            return new ResponseEntity<>(createErrorResponse("Water volume must be greater than zero"), HttpStatus.BAD_REQUEST);
        }
        if (initialChlorine < 0) {
            return new ResponseEntity<>(createErrorResponse("Initial chlorine must be a positive value"), HttpStatus.BAD_REQUEST);
        }
        if (initialPh < 0 || initialPh > 14) {
            return new ResponseEntity<>(createErrorResponse("Initial pH must be between 0 and 14"), HttpStatus.BAD_REQUEST);
        }
        if (initialTurbidity < 0) {
            return new ResponseEntity<>(createErrorResponse("Initial turbidity must be a positive value"), HttpStatus.BAD_REQUEST);
        }

        // Call the prediction service
        float[] predictions = pService.predict(waterVolume, initialChlorine, initialPh, initialTurbidity);

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("Applied Chlorine (KG)", predictions[0]);
        response.put("Applied Calcium Carbonate (KG)", predictions[1]);
        response.put("Applied PAC (KG)", predictions[2]);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        return errorResponse;
    }
}
