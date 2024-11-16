package com.waterboard.waterqualityprediction.dto;

import com.waterboard.waterqualityprediction.models.SMSMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String phoneNumber;
    private String message;
    private String messageId;
    private String eventId;
    private String eventType;

    public static MessageDto init(SMSMessage smsMessage) {
        var dto = new MessageDto();
        dto.setPhoneNumber(smsMessage.getPhoneNumber());
        dto.setMessage(smsMessage.getMessage());
        return dto;
    }
}
