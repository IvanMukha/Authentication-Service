package com.mukha.authservice.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String login) {
        super("User with this login: "+login+" already exists");
    }
}
