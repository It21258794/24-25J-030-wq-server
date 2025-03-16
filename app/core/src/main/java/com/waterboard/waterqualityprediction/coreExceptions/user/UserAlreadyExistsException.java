package com.waterboard.waterqualityprediction.coreExceptions.user;

import com.waterboard.waterqualityprediction.commonExceptions.ConflictException;
import com.waterboard.waterqualityprediction.commonExceptions.ExceptionType;

public class UserAlreadyExistsException extends ConflictException {
  public UserAlreadyExistsException(ExceptionType type, String message, Object... params) {
    super(type, message, params);
  }

  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException(String message, Object... params) {
    super(message, params);
  }
}
