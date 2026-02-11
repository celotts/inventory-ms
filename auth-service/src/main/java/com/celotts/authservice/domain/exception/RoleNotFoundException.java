package com.celotts.authservice.domain.exception;

public class RoleNotFoundException extends BaseAuthException {
    public RoleNotFoundException(String role) {
        super("auth.error.role.not-found", role);
    }
}
