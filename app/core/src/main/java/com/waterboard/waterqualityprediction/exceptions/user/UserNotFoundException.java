package com.waterboard.waterqualityprediction.exceptions.user;

import com.waterboard.waterqualityprediction.exceptions.http.BadRequestException;

public class UserNotFoundException extends BadRequestException {
    public UserNotFoundException(String message) {
        super(message);
        this.setType(ExType.USER_NOT_FOUND);
    }
}
