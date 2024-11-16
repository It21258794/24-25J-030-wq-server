package com.waterboard.waterqualityprediction.controllers;

import com.waterboard.waterqualityprediction.NotificationModule;
import com.waterboard.waterqualityprediction.dto.MailDto;
import com.waterboard.waterqualityprediction.dto.MessageDto;
import com.waterboard.waterqualityprediction.models.Mail;
import com.waterboard.waterqualityprediction.models.SMSMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NotificationController {

    @Autowired
    NotificationModule notificationModule;

    @PostMapping("system/send/mail")
    public ResponseEntity<MailDto> sendEmail(@RequestBody MailDto mailDto) {
        mailDto = notificationModule.sendEmail(Mail.init(mailDto));
        return ResponseEntity.ok(mailDto);
    }

    @PostMapping("/system/send/sms")
    public ResponseEntity<MessageDto> sendSms(@RequestBody MessageDto messageDto) {
        messageDto = notificationModule.sendSMS(SMSMessage.init(messageDto));
        return ResponseEntity.ok(messageDto);
    }
}
