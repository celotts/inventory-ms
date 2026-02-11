package com.celotts.authservice.domain.exception;

public class UsernameAlreadyExistsException extends BaseAuthException {
    public UsernameAlreadyExistsException(String username) {
        super("auth.error.username.taken", username);
    }
}
