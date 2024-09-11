package com.jdbayer.prueba.api.exceptions;

import java.io.Serial;

public class NotExistUserException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -4738996582480855811L;

  public NotExistUserException(String message) {
        super(message);
    }
}
