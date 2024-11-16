package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class NotificationModuleConfigs {

    @Value("${notification.email.sender.name}")
    private String notificationEmailSenderName;

    @Value("${notification.email.sender.email}")
    private String notificationEmailSenderEmail;

    @Value("${notification.redirect:false}")
    private boolean notificationRedirect;

    @Value("${notification.redirect_email:kaveeshakarunasena@gmail.com}")
    private String notificationRedirectEmail;

    @Value("${notification.redirect_phone:null}")
    private String notificationRedirectPhone;

    @Value("${notification.message.twilio.phone_number:}")
    private String twilioPhoneNumber;

    @Value("${notification.message.twilio.account_sid:}")
    private String twilioAccountSid;

    @Value("${notification.message.twilio.auth_token:}")
    private String twilioAuthToken;

    @Value("${notification.message.provider:mock}")
    private String notificationMessageServiceProvider;
}
