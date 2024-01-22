package com.example.demo.exception;

public class MatchingPasswordsException extends RuntimeException {
    public MatchingPasswordsException(String message) {
        super(message);
    }
}
