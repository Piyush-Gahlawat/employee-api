package com.example.employeeapi.exception;

public class InvalidEmployeeException extends RuntimeException {
    public InvalidEmployeeException(String message) {
        super(message);
    }

    public InvalidEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
