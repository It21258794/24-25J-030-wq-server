package com.waterboard.sensormanagment.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.waterboard.sensormanagment.services.DashboardService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/ping")
    public String testEndpoint() {
        return "🚀 Modbus Service Works!";
    }

    @GetMapping("/read/{start}/{quantity}")
    public List<Integer> readRegisters(@PathVariable int start, @PathVariable int quantity) throws Exception {
        return dashboardService.readHoldingRegisters(start, quantity);
    }
}
