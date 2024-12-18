package com.waterboard.waterqualityprediction.exceptions.notification;

public class MailNotSendException extends RuntimeException {
    public MailNotSendException(String message) {
        super(message);
    }
}
