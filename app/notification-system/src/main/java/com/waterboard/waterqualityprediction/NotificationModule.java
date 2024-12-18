package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.dto.MailDto;
import com.waterboard.waterqualityprediction.dto.MessageDto;
import com.waterboard.waterqualityprediction.models.Mail;
import com.waterboard.waterqualityprediction.models.SMSMessage;
import com.waterboard.waterqualityprediction.services.email.EmailService;
import com.waterboard.waterqualityprediction.services.message.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationModule {

    public static final String EMAIL_QUEUE = "com.water.quality.queue.email";
    public static final String SMS_QUEUE = "com.water.quality.queue.sms";

    @Autowired
    NotificationModuleConfigs notificationModuleConfigs;

    @Autowired
    EmailService emailService;

    @Autowired
    MessageService messageService;

    @Autowired
    QueueModule queueModule;

    @Autowired
    CheckExternalApi checkExternalApi;

    @Autowired
    NotificationMessageFactory notificationMessageFactory;

    public MailDto sendEmail(Mail mail) {
        if (mail.getFrom() == null) {
            mail.setFrom(new Mail.MailAddress(notificationModuleConfigs.getNotificationEmailSenderName(),
                    notificationModuleConfigs.getNotificationEmailSenderEmail()));
        }
        if (notificationModuleConfigs.isNotificationRedirect()) {
            String emailDebugInfo = "Email redirected from : ";
            if (!checkExternalApi.isExternal()) {
                for (Mail.MailAddress mailAddress : mail.getTo()) {
                    log.info("notification redirection set to true. email receiver change from={}, to={}", mailAddress.getEmail(),
                            notificationModuleConfigs.getNotificationRedirectEmail());
                    emailDebugInfo = emailDebugInfo.concat(mailAddress.getEmail()).concat(",");
                    mailAddress.setEmail(notificationModuleConfigs.getNotificationRedirectEmail());
                }
            }
            else {
                int count = 0;
                for (Mail.MailAddress email : mail.getTo()) {
                    mail.getTo().get(count).setEmail(email.getEmail());
                    count++;
                }
                checkExternalApi.setExternal(false);
            }
            mail.getHtmlTemplate().getProps().put("_debug_info_", emailDebugInfo);
        }
        log.info("sending emails = {}", mail.getToAsString());
        return emailService.sendEmail(mail);
    }

    public void sendEmailAsync(Mail mail) {
        log.info("sending NotificationModule queue async emails = {}", mail.getToAsString());
        queueModule.sendMessage(EMAIL_QUEUE, JSON.objectToString(mail));
    }

    public MessageDto sendSMS(SMSMessage message) {
        log.info("sending sms, to = {},  sms = {}", message.getPhoneNumber(), message.getMessage());
        if (notificationModuleConfigs.isNotificationRedirect()) {
            log.info("notification redirection set to true. sms receiver change from={}, to={}", message.getPhoneNumber(),
                    notificationModuleConfigs.getNotificationRedirectPhone());
            message.setMessage(message.getMessage() + "\n\n--SMS redirected from : " + message.getPhoneNumber());
            message.setPhoneNumber(notificationModuleConfigs.getNotificationRedirectPhone());
        }
        messageService = notificationMessageFactory.getMessageService(message.getPhoneNumber());
        MessageDto messageDto = messageService.sendMessage(message);
        return messageDto;
    }

    public void sendSmsAsync(SMSMessage sms) {
        log.info("sending queue async, to = {},  sms = {}", sms.getPhoneNumber(), sms.getMessage());
        queueModule.sendMessage(SMS_QUEUE, JSON.objectToString(sms));
    }
}
