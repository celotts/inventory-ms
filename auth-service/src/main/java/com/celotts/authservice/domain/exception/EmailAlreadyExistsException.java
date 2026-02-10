package com.celotts.authservice.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Error: Email '" + email + "' is already in use!");
    }
}
