package com.waterboard.waterqualityprediction.exceptions.user;

import com.waterboard.waterqualityprediction.exceptions.http.BadRequestException;

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