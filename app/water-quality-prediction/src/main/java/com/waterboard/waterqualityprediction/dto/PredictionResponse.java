package com.waterboard.waterqualityprediction.dto;

import com.waterboard.waterqualityprediction.modles.Prediction;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class PredictionResponse {
    private List<Prediction> predictions;
}
