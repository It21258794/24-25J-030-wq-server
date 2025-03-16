package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.DateHelper;
import com.waterboard.waterqualityprediction.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class PredictionSchedulerService {

    @Autowired
    PredictionService predictionService;

    @Scheduled(cron = "${prediction.fetch.cron}")
    public void predictionScheduler() {
        String uuid = "wqs-"+ Generator.getUUID();
        log.info("running water quality prediction scheduler = {}, UUID = {}", DateHelper.now(), uuid);
        predictionService.fetchAndStorePredictions(30,uuid);
    }

    @Scheduled(cron = "${prediction.fetch.weather.cron}")
    public void weatherPredictionScheduler() {
        String uuid = "wqs-"+ Generator.getUUID();
        log.info("running weather prediction scheduler = {}, UUID = {}", DateHelper.now(), uuid);
        predictionService.fetchAndStoreWeatherData(7,uuid);
    }
}
