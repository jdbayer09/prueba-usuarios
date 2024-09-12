package com.jdbayer.prueba.api.exceptions;

import java.io.Serial;

public class ErrorTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2549554351080803297L;

    public ErrorTokenException(String message) {
        super(message);
    }
}
