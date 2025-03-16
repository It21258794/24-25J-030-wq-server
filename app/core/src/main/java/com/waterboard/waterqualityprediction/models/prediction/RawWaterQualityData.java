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

@Document("wm_scada_data")
@Getter
@Setter
public class RawWaterQualityData extends AuditModel {
    @Id
    private String id;
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant date;
    private String centerId;
    private List<WqDataReading> wqDataReadings;
}

