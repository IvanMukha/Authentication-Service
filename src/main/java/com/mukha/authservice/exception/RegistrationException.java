package com.mukha.authservice.exception;

public class RegistrationException extends RuntimeException {
    public RegistrationException() {
        super("Failed to creation user");
    }
}
