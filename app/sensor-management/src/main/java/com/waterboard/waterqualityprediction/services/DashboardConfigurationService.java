package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.models.DashboardConfiguration;
import com.waterboard.waterqualityprediction.repository.DashboardConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DashboardConfigurationService {

    @Autowired
    private DashboardConfigurationRepository dashboardConfigurationRepository;

    public DashboardConfiguration saveDashboardConfiguration(DashboardConfiguration dashboardConfiguration) {
        return dashboardConfigurationRepository.save(dashboardConfiguration);
    }

    public DashboardConfiguration getDashboardConfigurationByUserId(String userId) {
        return dashboardConfigurationRepository.findByUserId(userId);
    }

    public Optional<DashboardConfiguration> findByUserId(String userId) {
        return Optional.ofNullable(dashboardConfigurationRepository.findByUserId(userId));  // Assumes you have a method in the repository
    }


}
