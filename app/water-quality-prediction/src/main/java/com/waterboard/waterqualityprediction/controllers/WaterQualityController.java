package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.PaginationDto;
import com.waterboard.waterqualityprediction.dto.PredictionRequest;
import com.waterboard.waterqualityprediction.dto.PredictionResult;
import com.waterboard.waterqualityprediction.dto.WeatherAndPredictionDTO;
import com.waterboard.waterqualityprediction.models.prediction.WaterQualityParameter;
import com.waterboard.waterqualityprediction.services.DataService;
import com.waterboard.waterqualityprediction.services.PredictionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/water-quality")
public class WaterQualityController {

    @Autowired
    private DataService waterQualityService;

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/scada")
    public void importData(@RequestBody String jsonData) {
        try {
            waterQualityService.saveWaterQualityData(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/weather")
    public void importWeatherData(@RequestBody String jsonData) {
        try {
            waterQualityService.saveWeatherData(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/parameters")
    public ResponseEntity<List<WaterQualityParameter>> addWaterQualityParameters(@RequestBody List<WaterQualityParameter> parameters) {
        List<WaterQualityParameter> savedParameters = waterQualityService.saveWaterQualityParameters(parameters);
        return ResponseEntity.ok(savedParameters);
    }

    @GetMapping("/predictions")
    public ResponseEntity<PredictionResult> getPredictions(
            @RequestParam("days") int days,
            @RequestParam("parameter") String parameter,
            @RequestParam("isPast") boolean isPast) {

        return ResponseEntity.ok(predictionService.getPredictions(PredictionRequest.initPredictionRequest(days, parameter, isPast)));
    }

    @GetMapping("/future-predictions")
    public ResponseEntity<PaginationDto<WeatherAndPredictionDTO>> getWeatherAndPredictions(
            @RequestParam String parameterName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginationDto<WeatherAndPredictionDTO> response =
                predictionService.getWeatherAndPredictions(parameterName, page, size);

        return ResponseEntity.ok(response);
    }
}
