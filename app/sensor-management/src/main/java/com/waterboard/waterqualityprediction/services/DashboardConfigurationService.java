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
        Optional<DashboardConfiguration> existingConfig = Optional.ofNullable(dashboardConfigurationRepository.findByUserId(dashboardConfiguration.getUserId()));

        if (existingConfig.isPresent()) {
            DashboardConfiguration configToUpdate = existingConfig.get();
            configToUpdate.setTurbidity(dashboardConfiguration.isTurbidity());
            configToUpdate.setPh(dashboardConfiguration.isPh());
            configToUpdate.setConductivity(dashboardConfiguration.isConductivity());
            configToUpdate.setTurbidityGraph(dashboardConfiguration.isTurbidityGraph());
            configToUpdate.setPhGraph(dashboardConfiguration.isPhGraph());
            configToUpdate.setConductivityGraph(dashboardConfiguration.isConductivityGraph());
            configToUpdate.setChart(dashboardConfiguration.isChart());
            configToUpdate.setTotalAnalysis(dashboardConfiguration.isTotalAnalysis());
            configToUpdate.setTotalTreatedAnalysis(dashboardConfiguration.isTotalTreatedAnalysis());
            configToUpdate.setTreatedChart(dashboardConfiguration.isTreatedChart());
            configToUpdate.setLimeUsageChart(dashboardConfiguration.isLimeUsageChart());
            configToUpdate.setPacChart(dashboardConfiguration.isPacChart());
            configToUpdate.setChlorineUsageChart(dashboardConfiguration.isChlorineUsageChart());
            configToUpdate.setLimeUsage(dashboardConfiguration.isLimeUsage());
            configToUpdate.setPacUsage(dashboardConfiguration.isPacUsage());
            configToUpdate.setChlorineUsage(dashboardConfiguration.isChlorineUsage());

            return dashboardConfigurationRepository.save(configToUpdate);
        } else {
            return dashboardConfigurationRepository.save(dashboardConfiguration);
        }
    }


    public DashboardConfiguration getDashboardConfigurationByUserId(String userId) {
        return dashboardConfigurationRepository.findByUserId(userId);
    }

    public Optional<DashboardConfiguration> findByUserId(String userId) {
        return Optional.ofNullable(dashboardConfigurationRepository.findByUserId(userId));  // Assumes you have a method in the repository
    }


}
