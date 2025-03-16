package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PredictionModuleConfig{

    @Value("${open_weather.apiKey}")
    private String openWeatherApiKey;

    @Value("${open_weather.url}")
    private String openWeatherUrl;

    @Value("${open_weather.lat}")
    private Float openWeatherLatitude;

    @Value("${open_weather.lon}")
    private Float openWeatherLongitude;
}
