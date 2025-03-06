package com.waterboard.waterqualityprediction.commonExceptions.http;

import com.waterboard.waterqualityprediction.commonExceptions.ExType;
import com.waterboard.waterqualityprediction.commonExceptions.ExceptionType;
import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends BaseException {

    public TooManyRequestsException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.TOO_MANY_REQUESTS);
    }

    public TooManyRequestsException(String message, Object... params) {
        super(message, params);
        setType(ExType.TOO_MANY_REQUESTS);
    }

    public TooManyRequestsException(String message) {
        super(message);
        setType(ExType.TOO_MANY_REQUESTS);
    }

    public TooManyRequestsException(ExceptionType type, String message) {
        super(type, message);
    }

    public TooManyRequestsException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public TooManyRequestsException(Exception rootException, ExceptionType type, String message, Object... params) {
        super(rootException, type, message, params);
    }


    @Override
    public HttpStatus getCode() {
        return HttpStatus.TOO_MANY_REQUESTS;
    }
}