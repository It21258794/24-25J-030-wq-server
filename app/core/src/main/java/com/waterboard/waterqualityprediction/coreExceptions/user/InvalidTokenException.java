package com.waterboard.waterqualityprediction.coreExceptions.user;

import com.waterboard.waterqualityprediction.coreExceptions.http.BadRequestException;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException(String message) {
        super(message);
        setType(ExType.INVALID_TOKEN);
    }

    public InvalidTokenException(String message,ExType type) {
        super(message);
        setType(type);
    }

}