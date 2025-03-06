package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.models.Mail;
import com.waterboard.waterqualityprediction.models.SMSMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class NotificationModuleListeners {

    public static final String EMAIL_QUEUE = "com.water.quality.queue.email";
    public static final String SMS_QUEUE = "com.water.quality.queue.sms";

    @Autowired
    private NotificationModule notificationModule;

    @RabbitListener(queuesToDeclare = @Queue(name = EMAIL_QUEUE))
    private void jmsEmailEndpoint(String message) {
        log.info("receive jms message destination = {}, data = {}", EMAIL_QUEUE, StringUtils.truncate(message, 200));
        try {
            Mail mail = JsonUtils.stringToObject(message, Mail.class);
            this.notificationModule.sendEmail(mail);
        } catch (Exception e) {
            log.error("error sending email. error={}", e.getMessage());
            throw e;
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(name = SMS_QUEUE))
    private void jmsSmsEndpoint(String message) {
        log.info("receive jms message destination = {}, data = {}", SMS_QUEUE, StringUtils.truncate(message, 200));
        try {
            SMSMessage smsMessage = JsonUtils.stringToObject(message, SMSMessage.class);
            this.notificationModule.sendSMS(smsMessage);;
        } catch (Exception e) {
            log.error("error sending sms. error={}", e.getMessage());
            throw e;
        }
    }
}
