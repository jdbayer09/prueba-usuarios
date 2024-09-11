package com.jdbayer.prueba.api.exceptions;

import java.io.Serial;

public class NotExistPhoneException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4671445210509805067L;

    public NotExistPhoneException(String message) {
        super(message);
    }
}
