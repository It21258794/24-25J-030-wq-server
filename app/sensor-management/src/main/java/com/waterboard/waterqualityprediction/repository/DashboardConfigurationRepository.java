package com.waterboard.waterqualityprediction.repository;

import com.waterboard.waterqualityprediction.models.DashboardConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DashboardConfigurationRepository extends JpaRepository<DashboardConfiguration, Long> {
    DashboardConfiguration findByUserId(String userId);
}
