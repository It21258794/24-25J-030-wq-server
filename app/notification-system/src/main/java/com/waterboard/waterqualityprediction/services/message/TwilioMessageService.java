package com.waterboard.waterqualityprediction.services.message;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.waterboard.waterqualityprediction.NotificationModuleConfigs;
import com.waterboard.waterqualityprediction.dto.MessageDto;
import com.waterboard.waterqualityprediction.models.SMSMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioMessageService implements MessageService {

    private String twilioPhoneNumber;

    public TwilioMessageService(NotificationModuleConfigs notificationModuleConfigs) {
        log.info("initializing TwilioMessageService service. phone = {}, account id = {}", notificationModuleConfigs.getTwilioPhoneNumber(), notificationModuleConfigs.getTwilioAccountSid());
        String twilioAccountSid = notificationModuleConfigs.getTwilioAccountSid();
        String twilioAuthToken = notificationModuleConfigs.getTwilioAuthToken();
        this.twilioPhoneNumber = notificationModuleConfigs.getTwilioPhoneNumber();
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    @Override
    public MessageDto sendMessage(SMSMessage smsMessage) {
        log.info("send sms with twilio message = {}", smsMessage);
        PhoneNumber to = new PhoneNumber(smsMessage.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(this.twilioPhoneNumber);
        Message message = Message.creator(to, from, smsMessage.getMessage()).create();
        MessageDto messageDto = MessageDto.init(smsMessage);
        messageDto.setMessageId(message.getSid());
        log.info("twilio sms result. id={}, message={}",messageDto.getMessageId(), smsMessage);
        return messageDto;
    }
}
