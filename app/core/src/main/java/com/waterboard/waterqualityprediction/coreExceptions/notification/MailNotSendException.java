package com.waterboard.waterqualityprediction.coreExceptions.notification;

public class MailNotSendException extends RuntimeException {
    public MailNotSendException(String message) {
        super(message);
    }
}
