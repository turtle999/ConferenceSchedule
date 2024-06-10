package com.example.Conference.exception;

import com.example.Conference.domain.common.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApiErrorException extends RuntimeException {

    private final ApiErrorDto error;

    public ApiErrorException(ApiErrorDto error) {
        this.error = error;
    }

    public ApiErrorDto getError() {
        return error;
    }

}
