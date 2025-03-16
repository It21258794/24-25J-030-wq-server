package com.waterboard.waterqualityprediction.models.prediction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.waterboard.waterqualityprediction.AuditModel;
import com.waterboard.waterqualityprediction.InstantDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document("wm_weather_data")
public class WeatherData extends AuditModel {
    @Id
    private String id;
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant date;
    private String location;
    private List<WeatherReading> readings;
}
