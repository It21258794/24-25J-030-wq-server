package com.waterboard.waterqualityprediction.repositories;

import com.waterboard.waterqualityprediction.models.prediction.WeatherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends MongoRepository<WeatherData,String> {
    List<WeatherData> findByDateBetween(Instant startDate, Instant endDate);
    Page<WeatherData> findByDateBetween(Instant start, Instant end, Pageable pageable);
    Optional<WeatherData> findByDateAndLocation(Instant date, String location);
}
