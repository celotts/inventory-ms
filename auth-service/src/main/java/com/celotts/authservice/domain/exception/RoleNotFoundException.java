package com.celotts.authservice.domain.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super("Error: Role '" + role + "' is not found.");
    }
}
