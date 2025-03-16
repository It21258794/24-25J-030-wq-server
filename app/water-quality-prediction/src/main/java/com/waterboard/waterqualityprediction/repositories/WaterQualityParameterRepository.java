package com.waterboard.waterqualityprediction.repositories;

import com.waterboard.waterqualityprediction.models.prediction.WaterQualityParameter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WaterQualityParameterRepository extends MongoRepository<WaterQualityParameter, String> {
    Optional<WaterQualityParameter> findByName(String name);
}
