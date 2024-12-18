package com.waterboard.waterqualityprediction.exceptions.user;

import com.waterboard.waterqualityprediction.exceptions.http.BadRequestException;

public class InvalidInputException extends BadRequestException {
    public InvalidInputException(String message) {
        super(message);
        this.setType(ExType.INVALID_INPUT);
    }

    public InvalidInputException(String message,ExType exType) {
        super(message);
        this.setType(exType);
    }

    public InvalidInputException(String message, Object... params) {
        super(message, params);
    }
}
