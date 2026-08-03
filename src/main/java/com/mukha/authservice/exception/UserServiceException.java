package com.mukha.authservice.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException() {
        super("user-service returned an empty response");
    }
}
