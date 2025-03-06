package com.waterboard.waterqualityprediction.coreExceptions.user;


import com.waterboard.waterqualityprediction.commonExceptions.http.BadRequestException;

public class UserNotFoundException extends BadRequestException {
    public UserNotFoundException(String message) {
        super(message);
        this.setType(ExType.USER_NOT_FOUND);
    }
}
