package com.waterboard.dailychemicalprediction.service;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import ml.dmlc.xgboost4j.java.*;

@Service
public class DailypredictionService {

    private Booster booster;

    public DailypredictionService() {
        try (InputStream modelStream = getClass().getClassLoader().getResourceAsStream("DailyPrediction.bin")) {
            if (modelStream == null) {
                throw new RuntimeException("Model file not found in resources folder");
            }
            booster = XGBoost.loadModel(modelStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load XGBoost model", e);
        }
    }

    // Method to perform prediction
    public float[] predict(float waterVolume, float initialChlorine, float initialPh, float initialTurbidity) {
        // Validate input data before proceeding
        if (waterVolume <= 0) {
            throw new IllegalArgumentException("Water volume must be greater than zero");
        }
        if (initialChlorine < 0) {
            throw new IllegalArgumentException("Initial chlorine must be a positive value");
        }
        if (initialPh < 0 || initialPh > 14) {
            throw new IllegalArgumentException("Initial pH must be between 0 and 14");
        }
        if (initialTurbidity < 0) {
            throw new IllegalArgumentException("Initial turbidity must be a positive value");
        }

        try {
            // Prepare the input data for prediction (flatten the array to match DMatrix input expectations)
            float[] input = {waterVolume, initialChlorine, initialPh, initialTurbidity};

            // Create DMatrix with the input data
            DMatrix dMatrix = new DMatrix(input, 1, input.length);

            // Perform prediction using the loaded booster model
            float[][] predictions = booster.predict(dMatrix);

            // Return the predictions for the first instance
            return predictions[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to perform prediction", e);
        }
    }
}
