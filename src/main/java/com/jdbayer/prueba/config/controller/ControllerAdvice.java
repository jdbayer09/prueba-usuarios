package com.jdbayer.prueba.config.controller;

import com.jdbayer.prueba.api.exceptions.ErrorTokenException;
import com.jdbayer.prueba.api.exceptions.NotExistPhoneException;
import com.jdbayer.prueba.api.exceptions.NotExistUserException;
import com.jdbayer.prueba.api.models.responses.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handlerUncaughtException(Throwable t) {
        return buildErrorResponse(t, t.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(PRECONDITION_FAILED)
    public ResponseEntity<ErrorResponse> invalidRequestErrorHandler(final MethodArgumentNotValidException e) {

        var errors =
                e.getBindingResult().getAllErrors().stream()
                        .filter(Objects::nonNull)
                        .map(this::getValidationErrorMessage)
                        .toList();
        return buildErrorResponse(e, String.join(", ", errors), PRECONDITION_FAILED);
    }

    public String getValidationErrorMessage(final ObjectError error) {
        final var errorMessage = new StringBuilder();
        if (error instanceof FieldError fe) {
            errorMessage.append("<").append(fe.getField()).append("> - ");
        }
        errorMessage.append(error.getDefaultMessage());
        return errorMessage.toString();
    }

    @ExceptionHandler({ObjectNotFoundException.class, NotExistPhoneException.class, NotExistUserException.class, ErrorTokenException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlerObjectNotFoundException(final ObjectNotFoundException t) {
        return buildErrorResponse(t, t.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(PRECONDITION_FAILED)
    public ResponseEntity<ErrorResponse> handlerIllegalArgumentException(final IllegalArgumentException t) {
        return buildErrorResponse(t, t.getMessage(), PRECONDITION_FAILED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ErrorResponse> handlerDataIntegrityViolationException(final DataIntegrityViolationException t) {
        String message;
        try {
            message = messageFkUnique(t.getCause().getCause().getMessage().toLowerCase());
        } catch (Exception ex) {
            message = "Unexpected error: " + t.getCause().getCause().getMessage();
        }

        return buildErrorResponse(t, message, CONFLICT);
    }

    private String messageFkUnique(String error) {
        var message = "Unexpected error: " + error;
        var errors = error.split("\\(")[2].split("\\)");
        var val = errors[0];

        if (error.contains("user_unique_email"))
            message = "The E-mail entered is already registered (" + val + ")";
        else if (error.contains("user_unique_phone"))
            message = "The phone entered is already registered (" + val + ")";

        return message;
    }


    /**
     * Builds the {@code ErrorResponse} object to serve all error request and response generic message
     *
     * @param e          Exception thrown by the handler itself
     * @param message    Message to be shown in the consumer request
     * @param httpStatus HTTP status to be sent it to the consumer
     * @return ErrorRepose
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Throwable e, String message, HttpStatus httpStatus) {
        log.error(message, e);
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
    }
}
