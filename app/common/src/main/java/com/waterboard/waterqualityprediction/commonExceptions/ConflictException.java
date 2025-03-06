package com.waterboard.waterqualityprediction.commonExceptions;

import com.waterboard.waterqualityprediction.commonExceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
  public ConflictException(Exception rootException, String message, Object... params) {
    super(rootException, message, params);
    setType(ExType.CONFLICT);
  }

  public ConflictException(String message, Object... params) {
    super(message, params);
    setType(ExType.CONFLICT);
  }

  public ConflictException(String message) {
    super(message);
    setType(ExType.CONFLICT);
  }

  public ConflictException(ExceptionType type, String message) {
    super(type, message);
  }

  public ConflictException(ExceptionType type, String message, Object... params) {
    super(type, message, params);
  }

  public ConflictException(Exception rootException, ExceptionType type, String message, Object... params) {
    super(rootException, type, message, params);
  }


  @Override
  public HttpStatus getCode() {
    return HttpStatus.CONFLICT;
  }
}
