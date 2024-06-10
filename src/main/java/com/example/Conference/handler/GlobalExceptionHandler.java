package com.example.Conference.handler;

import com.example.Conference.domain.common.ApiErrorDto;
import com.example.Conference.domain.common.ApiFieldValidationSubErrorDto;
import com.example.Conference.exception.ApiErrorException;
import com.example.Conference.exception.DuplicateTitleException;
import com.example.Conference.exception.InternalServerErrorException;
import com.example.Conference.helper.Enums;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(Enums.ApiErrorCodes.FIELD_VALIDATION_ERRORS)
                .message(ex.getMessage())
                .subErrors(new ArrayList<>())
                .build();

        for (FieldError fieldError : fieldErrors) {
            error.getSubErrors().add(
                    new ApiFieldValidationSubErrorDto(
                            fieldError.getObjectName(),
                            fieldError.getField(),
                            fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()
                    )
            );
        }
        logger.error("Validation error: ", ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(Enums.ApiErrorCodes.FIELD_VALIDATION_ERRORS)
                .message("Duration can be integer or lightning, no other option." + ex.getMessage())
                .subErrors(new ArrayList<>())
                .build();

        logger.error("Number format exception: ", ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(Enums.ApiErrorCodes.FIELD_VALIDATION_ERRORS)
                .message("Validation error: " + ex.getMessage())
                .subErrors(new ArrayList<>())
                .build();

        logger.error("Constraint violation: ", ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(ApiErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiErrorException(ApiErrorException ex) {
        logger.error("API error: ", ex);
        return new ResponseEntity<>(ex.getError(), ex.getError().getStatus());
    }

    @ExceptionHandler(DuplicateTitleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicateTitleException(DuplicateTitleException ex) {
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.CONFLICT)
                .errorCode(Enums.ApiErrorCodes.TALK_CREATE_TITLE_ALREADY_EXISTS)
                .message("Duplicate Title " + ex.getMessage())
                .subErrors(new ArrayList<>())
                .build();

        logger.error("Duplicate title: ", ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleInternalServerErrorException(InternalServerErrorException ex) {
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(Enums.ApiErrorCodes.INTERNAL_SERVER_ERROR)
                .message("Internal server error: " + ex.getMessage())
                .subErrors(new ArrayList<>())
                .build();

        logger.error("Internal server error: ", ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorCode(Enums.ApiErrorCodes.ENTITY_NOT_FOUND)
                .message(ex.getMessage())
                .subErrors(new ArrayList<>())
                .build();

        logger.error("Entity not found: ", ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

}
