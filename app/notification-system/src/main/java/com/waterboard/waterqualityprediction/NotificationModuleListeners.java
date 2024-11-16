package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.common.JSON;
import com.waterboard.waterqualityprediction.models.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

@Component
@Slf4j
public class NotificationModuleListeners {

    public static final String EMAIL_QUEUE = "com.water.quality.queue.email";

    @Autowired
    private NotificationModule notificationModule;

    @RabbitListener(queuesToDeclare = @Queue(name = EMAIL_QUEUE))
    private void jmsEmailEndpoint(String message) {
        log.info("receive jms message destination = {}, data = {}", EMAIL_QUEUE, StringUtils.truncate(message, 200));
        try {
            Mail mail = JSON.stringToObject(message, Mail.class);
            this.notificationModule.sendEmail(mail);
        } catch (Exception e) {
            log.error("error sending email. error={}", e.getMessage());
            throw e;
        }
    }
}
