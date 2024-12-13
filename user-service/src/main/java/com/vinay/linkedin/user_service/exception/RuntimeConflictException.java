package com.vinay.linkedin.user_service.exception;

public class RuntimeConflictException extends RuntimeException {
    public RuntimeConflictException() {
    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
