package com.mukha.authservice.exception;

public class RollbackFailureException extends RuntimeException {
    public RollbackFailureException(Long userId) {
        super("CRITICAL: Failed to delete user " + userId + " from DB during rollback!");
    }
}
