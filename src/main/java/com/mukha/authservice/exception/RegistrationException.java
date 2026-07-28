    package com.mukha.authservice.exception;

    import org.springframework.http.HttpStatus;

    public class RegistrationException extends RuntimeException {
        public RegistrationException() {
            super("Failed to creation user");
        }
    }
