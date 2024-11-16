package com.waterboard.waterqualityprediction.services.message;

import com.waterboard.waterqualityprediction.dto.MessageDto;
import com.waterboard.waterqualityprediction.models.SMSMessage;

public interface MessageService {
    MessageDto sendMessage(SMSMessage smsMessage);
}
