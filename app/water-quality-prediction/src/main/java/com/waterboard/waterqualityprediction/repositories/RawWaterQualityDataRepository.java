package com.waterboard.waterqualityprediction.repositories;

import com.waterboard.waterqualityprediction.models.prediction.RawWaterQualityData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface RawWaterQualityDataRepository  extends MongoRepository<RawWaterQualityData, String> {
    List<RawWaterQualityData> findByDateBetween(Instant startDate, Instant endDate);
}
