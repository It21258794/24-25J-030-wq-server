package com.waterboard.waterqualityprediction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PredictionResult {
    private List<String> dates;
    private List<Double> values;

    public static PredictionResult initPredictionResult(List<String> dates, List<Double> values) {
        PredictionResult dto = new PredictionResult();
        dto.setDates(dates);
        dto.setValues(values);
        return dto;
    }

}

