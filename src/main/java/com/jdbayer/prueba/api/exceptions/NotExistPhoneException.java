package com.jdbayer.prueba.api.exceptions;

public class NotExistPhoneException extends RuntimeException {
    public NotExistPhoneException(String message) {
        super(message);
    }
}
