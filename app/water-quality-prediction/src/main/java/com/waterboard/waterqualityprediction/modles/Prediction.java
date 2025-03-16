package com.waterboard.waterqualityprediction.modles;

import com.waterboard.waterqualityprediction.AuditModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document("wm_prediction")
@Getter
@Setter
public class Prediction extends AuditModel {
    @Id
    private String id;
    private Instant predictedDate;
    private double avgPh;
    private double avgConductivity;
    private double avgTurbidity;
    @Transient
    private String date;
}
