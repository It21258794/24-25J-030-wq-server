package com.waterboard.waterqualityprediction.models.prediction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.waterboard.waterqualityprediction.InstantDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Setter
public class WqDataReading {
    @Id
    private String id;
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant dateTime;
    private double ph;
    private double conductivity;
    private double turbidity;
}
