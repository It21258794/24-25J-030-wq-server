package com.waterboard.waterqualityprediction.components;

import com.waterboard.waterqualityprediction.services.PredictionSchedulerService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
//@ConditionalOnExpression("'${CONTAINER_ROLE}'=='scheduler' or '${CONTAINER_ROLE}'=='all'")
public class ApplicationSchedulerBeans {
    @Bean
    public PredictionSchedulerService wqPredictionSchedulerService() {return new PredictionSchedulerService();}
}
