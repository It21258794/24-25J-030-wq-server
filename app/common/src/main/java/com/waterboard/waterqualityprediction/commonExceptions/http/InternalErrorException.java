package com.waterboard.waterqualityprediction.commonExceptions.http;

import com.waterboard.waterqualityprediction.commonExceptions.ExType;
import org.springframework.http.HttpStatus;

public class InternalErrorException extends BaseException {

    public InternalErrorException() {
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(String message, Object... params) {
        super(message, params);
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(String message) {
        super(message);
        setType(ExType.INTERNAL_ERROR);
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}