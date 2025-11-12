package com.celotts.taxservice.infrastructure.common.error;

public enum ErrorCode {
    // Cliente (4xx)
    VALIDATION_ERROR,
    RESOURCE_NOT_FOUND,
    RESOURCE_ALREADY_EXISTS,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    CONFLICT,

    // Servidor (5xx)
    INTERNAL_ERROR,
    DATABASE_ERROR,
    INTEGRATION_ERROR,

    NOT_FOUND,
    ALREADY_EXISTS,
    INVALID_ARGUMENT
}
