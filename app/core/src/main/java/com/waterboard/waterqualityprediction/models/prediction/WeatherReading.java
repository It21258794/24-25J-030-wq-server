package com.waterboard.waterqualityprediction.models.prediction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.waterboard.waterqualityprediction.InstantDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Setter
public class WeatherReading {
    @Id
    private String id;
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant dateTime;
    private double temp;
    private int humidity;
    private double windSpeed;
    private int clouds;
    private double feelsLike;
    private int pressure;
    private double dewPoint;
    private int visibility;
    private int windDeg;
    private double rainfall;

    public static WeatherReading init(Instant dateTime,double temp,int humidity,double windSpeed, int clouds, double feelsLike, int pressure, double dewPoint, int visibility, int windDeg, double rainfall){
        WeatherReading reading = new WeatherReading();
        reading.setDateTime(dateTime);
        reading.setTemp(temp);
        reading.setHumidity(humidity);
        reading.setWindSpeed(windSpeed);
        reading.setClouds(clouds);
        reading.setFeelsLike(feelsLike);
        reading.setPressure(pressure);
        reading.setDewPoint(dewPoint);
        reading.setVisibility(visibility);
        reading.setWindDeg(windDeg);
        reading.setRainfall(rainfall);
        return reading;

    }
}
