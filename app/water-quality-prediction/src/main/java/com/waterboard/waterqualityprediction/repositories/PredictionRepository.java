package com.waterboard.waterqualityprediction.repositories;

import com.waterboard.waterqualityprediction.modles.Prediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface PredictionRepository extends MongoRepository<Prediction,String>, CustomPredictionRepository {
    boolean existsByPredictedDate(Instant date);
    Page<Prediction> findByPredictedDateBetween(Instant start, Instant end, Pageable pageable);
}

interface CustomPredictionRepository {
    public List<Prediction> findPredictionsByDays(int days,boolean isPast);
}

class CustomPredictionRepositoryImpl implements CustomPredictionRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Prediction> findPredictionsByDays(int days, boolean isPast) {
        Instant now = Instant.now();
        Instant targetDate = isPast ? now.minus(days, ChronoUnit.DAYS) : now.plus(days, ChronoUnit.DAYS);

        Query query = new Query();
        query.addCriteria(isPast
                ? Criteria.where("predictedDate").gte(targetDate).lte(now)
                : Criteria.where("predictedDate").gte(now).lte(targetDate)
        );

        return mongoTemplate.find(query, Prediction.class);
    }
}
