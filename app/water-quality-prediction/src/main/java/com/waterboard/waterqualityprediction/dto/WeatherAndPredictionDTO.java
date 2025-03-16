package com.waterboard.waterqualityprediction.dto;

import com.waterboard.waterqualityprediction.models.prediction.WeatherData;
import com.waterboard.waterqualityprediction.models.prediction.WeatherReading;
import com.waterboard.waterqualityprediction.modles.Prediction;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class WeatherAndPredictionDTO {
    private String id;
    private Instant date;
    private String location;
    private List<WeatherReading> weatherReadings;
    private double avgPh;
    private double avgConductivity;
    private double avgTurbidity;
    private double threshold;

    public static WeatherAndPredictionDTO init(WeatherData weatherData, Prediction prediction, double threshold) {
        WeatherAndPredictionDTO dto = new WeatherAndPredictionDTO();
        dto.date = weatherData.getDate();
        dto.location = weatherData.getLocation();
        dto.setWeatherReadings(weatherData.getReadings());
        dto.setAvgPh(prediction.getAvgPh());
        dto.setAvgConductivity(prediction.getAvgConductivity());
        dto.setAvgTurbidity(prediction.getAvgTurbidity());
        dto.setThreshold(threshold);
        return dto;
    }
}
