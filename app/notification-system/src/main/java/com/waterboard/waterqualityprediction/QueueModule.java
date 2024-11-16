package com.waterboard.waterqualityprediction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueModule {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(String destination, Object msg) {
        rabbitTemplate.convertAndSend(destination, msg);
    }
}
