package com.waterboard.waterqualityprediction.components;

import com.waterboard.waterqualityprediction.services.AppInitializeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppInitializationHandler implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private AppInitializeService appInitializeService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        appInitializeService.initialize();
    }
}
