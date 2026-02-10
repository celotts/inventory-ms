package com.celotts.authservice.domain.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Error: Username '" + username + "' is already taken!");
    }
}
