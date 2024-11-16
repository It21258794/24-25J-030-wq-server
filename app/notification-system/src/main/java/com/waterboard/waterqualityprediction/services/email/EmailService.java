package com.waterboard.waterqualityprediction.services.email;

import com.waterboard.waterqualityprediction.dto.MailDto;
import com.waterboard.waterqualityprediction.models.Mail;

public interface EmailService {
    MailDto sendEmail(Mail mail);
}
