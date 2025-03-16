package com.waterboard.waterqualityprediction.models.prediction;

import com.waterboard.waterqualityprediction.AuditModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("wm_parameter")
@Getter
@Setter
public class WaterQualityParameter extends AuditModel {
    @Id
    private String id;
    private String name;
    private double threshold;
}
