package com.waterboard.waterqualityprediction.commonExceptions;

import com.waterboard.waterqualityprediction.commonExceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizeException extends BaseException {
    public UnauthorizeException() {
    }

    public UnauthorizeException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizeException(String message, Object... params) {
        super(message, params);
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizeException(String message) {
        super(message);
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizeException(ExceptionType type, String message) {
        super(type, message);
    }

    public UnauthorizeException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public UnauthorizeException(Exception rootException, ExceptionType type, String message, Object... params) {
        super(rootException, type, message, params);
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
