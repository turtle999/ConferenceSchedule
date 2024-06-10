package com.example.Conference.exception;

public class DuplicateTitleException extends RuntimeException {
    public DuplicateTitleException(String message) {
        super(message);
    }
}
