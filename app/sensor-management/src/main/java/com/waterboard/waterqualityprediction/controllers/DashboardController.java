package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.models.DashboardConfiguration;
import com.waterboard.waterqualityprediction.models.TagValue;
import com.waterboard.waterqualityprediction.services.DashboardConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.waterboard.waterqualityprediction.services.DashboardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final DashboardConfigurationService dashboardConfigurationService;

    public DashboardController(DashboardService dashboardService, DashboardConfigurationService dashboardConfigurationService) {
        this.dashboardService = dashboardService;
        this.dashboardConfigurationService = dashboardConfigurationService;
    }

    @GetMapping("/fetchModbusData")
    public Map<Integer, Float> fetchModbusData() {
        return dashboardService.fetchModbusData();
    }

    @PostMapping("/saveDashboardConfiguration")
    public DashboardConfiguration saveDashboardConfiguration(@RequestBody DashboardConfiguration dashboardConfiguration) {
        return dashboardConfigurationService.saveDashboardConfiguration(dashboardConfiguration);
    }

    @GetMapping("/getDashboardConfiguration")
    public DashboardConfiguration getDashboardConfiguration(@RequestParam String userId) {
        return dashboardConfigurationService.getDashboardConfigurationByUserId(userId);
    }


}
