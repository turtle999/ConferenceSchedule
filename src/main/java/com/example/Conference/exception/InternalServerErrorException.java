package com.example.Conference.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {
    @SuppressWarnings("unused")
    private final Exception baseException;

    public InternalServerErrorException(Exception baseException, String message) {
        super(message);
        this.baseException = baseException;
    }
}
