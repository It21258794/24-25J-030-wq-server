package com.waterboard.waterqualityprediction.repository;

import com.waterboard.waterqualityprediction.models.RegisterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterDataRepository extends JpaRepository<RegisterData, Long> {
}
