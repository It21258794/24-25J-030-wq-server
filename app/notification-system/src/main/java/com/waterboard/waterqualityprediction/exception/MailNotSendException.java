package com.waterboard.waterqualityprediction.exception;

public class MailNotSendException extends RuntimeException{
    public MailNotSendException(String message) {
        super(message);
    }
}
