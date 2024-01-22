package com.example.demo.exception;

public class InvalidAdmissionIdException extends RuntimeException {
    public InvalidAdmissionIdException(String message) {
        super(message);
    }
}
