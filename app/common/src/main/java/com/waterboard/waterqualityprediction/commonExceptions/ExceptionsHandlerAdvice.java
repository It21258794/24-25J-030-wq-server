package com.waterboard.waterqualityprediction.commonExceptions;

import com.waterboard.waterqualityprediction.RequestDataProvider;
import com.waterboard.waterqualityprediction.commonExceptions.http.BadRequestException;
import com.waterboard.waterqualityprediction.commonExceptions.http.BaseException;
import com.waterboard.waterqualityprediction.commonExceptions.http.InternalErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
public class ExceptionsHandlerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    RequestDataProvider requestDataProvider;

    @ExceptionHandler(value = { BaseException.class})
    protected ResponseEntity<Object> handleCustomException(BaseException exception, HttpServletRequest request) {
        log.error("exception occurred [BaseException] type = {} error = {}",exception.getClass().getCanonicalName(), exception.getMessage());
        exception.printStackTrace();
        return exception.getJsonAsResponse(requestDataProvider.getRequestHash());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception exception) {
        log.error("exception occurred [Exception] error = {}", exception.getMessage());
        exception.printStackTrace();
        var internalExp = new InternalErrorException(exception.getMessage());
        return internalExp.getJsonAsResponse(requestDataProvider.getRequestHash());
    }

//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        log.error("exception occurred [Malformed exception] error = {}", ex.getMessage());
//        log.info("http response url = {}, response = {}",  request.getContextPath(), status.value());
//        ex.printStackTrace();
//        var exception = new BadRequestException("malformed request body. Please check for correct date/time formats and data types");
//        return exception.getJsonAsResponse(requestDataProvider.getRequestHash());
//    }


}

