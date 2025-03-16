package com.waterboard.waterqualityprediction.components;

import com.waterboard.waterqualityprediction.AnalyticsModuleListeners;
import com.waterboard.waterqualityprediction.NotificationModuleListeners;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnExpression("'${CONTAINER_ROLE}'=='queue' or '${CONTAINER_ROLE}'=='all'")
public class ApplicationListenerBeans {

    @Bean
    public AnalyticsModuleListeners analyticsModuleListeners() {
        return new AnalyticsModuleListeners();
    }

    @Bean
    public NotificationModuleListeners notificationModuleListeners() {
        return new NotificationModuleListeners();
    }


}
