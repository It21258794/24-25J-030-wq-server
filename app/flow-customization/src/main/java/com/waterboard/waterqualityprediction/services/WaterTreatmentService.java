package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.dto.WaterQualityRequest;
import com.waterboard.waterqualityprediction.models.WaterQuality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WaterTreatmentService {

    private static final Logger logger = LoggerFactory.getLogger(WaterTreatmentService.class);
    private final RestTemplate restTemplate;
    private final String flaskApiUrl = "http://13.53.192.110:5000/predict";

    public WaterTreatmentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WaterQuality predictTreatedWater(WaterQualityRequest request) {
        // Log the request payload
        logger.info("Sending request to Flask API: {}", request);

        // Set headers explicitly
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with headers and body
        HttpEntity<WaterQualityRequest> entity = new HttpEntity<>(request, headers);

        // Send the request and log the response
        WaterQuality response = restTemplate.postForObject(flaskApiUrl, entity, WaterQuality.class);
        logger.info("Received response from Flask API: {}", response);

        return response;
    }
}