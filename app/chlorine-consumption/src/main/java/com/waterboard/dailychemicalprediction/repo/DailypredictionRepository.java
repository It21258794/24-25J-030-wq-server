package com.waterboard.dailychemicalprediction.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.waterboard.dailychemicalprediction.model.Dailyprediction;

@Repository
public interface DailypredictionRepository extends JpaRepository<Dailyprediction,Long> {
    
}
