package com.waterboard.waterqualityprediction.exceptions.http;

import com.waterboard.waterqualityprediction.exceptions.ExType;
import com.waterboard.waterqualityprediction.exceptions.ExceptionType;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(String message, Object... params) {
        super(message, params);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(String message, List<String> errors) {
        super(message, errors);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(ExceptionType exceptionType, String message) {
        super(exceptionType, message);
    }

    public BadRequestException(String message) {
        super(message);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

}
