package com.celotts.authservice.domain.exception;

public class EmailAlreadyExistsException extends BaseAuthException {
    public EmailAlreadyExistsException(String email) {
        super("auth.error.email.taken", email);
    }
}
