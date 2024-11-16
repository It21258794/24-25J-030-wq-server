package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.services.message.MessageService;
import com.waterboard.waterqualityprediction.services.message.TwilioMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageFactory {
    @Autowired
    private NotificationModuleConfigs notificationModuleConfigs;

    public MessageService getMessageService(String phoneNumber) {
        return getMessageServiceByProvider(notificationModuleConfigs.getNotificationMessageServiceProvider());
    }

    private MessageService getMessageServiceByProvider(String notificationSmsServiceProvider) {
        switch (notificationSmsServiceProvider) {
            case "twilio":
                return new TwilioMessageService(notificationModuleConfigs);
            default:
                return null;
        }
    }
}
