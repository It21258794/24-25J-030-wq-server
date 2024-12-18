package com.waterboard.waterqualityprediction.exceptions.user;

import com.waterboard.waterqualityprediction.exceptions.ConflictException;
import com.waterboard.waterqualityprediction.exceptions.ExceptionType;

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
