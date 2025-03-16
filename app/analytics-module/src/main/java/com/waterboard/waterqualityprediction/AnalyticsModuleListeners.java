package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.models.analytics.AMEvent;
import com.waterboard.waterqualityprediction.repositories.AMEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AnalyticsModuleListeners {

    public static final String ANALYTICS_QUEUE = "com.water.quality.analytics.events";

    @Autowired
    private AMEventRepository amEventRepository;

    @RabbitListener(queuesToDeclare = @Queue(name = ANALYTICS_QUEUE))
    public void handleQueueMessage(String eventStr) {
        try {
            AMEvent event = JsonUtils.stringToObject(eventStr, AMEvent.class);
            this.amEventRepository.save(event);
        } catch (Exception e) {
            log.error("error saving analytics event. error = {}", e.getMessage());
        }
    }

}
