package com.waterboard.waterqualityprediction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PredictionRequest {
    private String id;
    private int days;
    private String parameter;
    private Boolean isPast;

    public static PredictionRequest initPredictionRequest(int days, String parameter, Boolean isPast) {
       PredictionRequest predictionRequest = new PredictionRequest();
        predictionRequest.setDays(days);
        predictionRequest.setParameter(parameter);
        predictionRequest.setIsPast(isPast);
        return predictionRequest;
    }
}
