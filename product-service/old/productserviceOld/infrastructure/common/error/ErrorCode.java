package com.celotts.productserviceOld.infrastructure.common.error;

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
    INTEGRATION_ERROR
}
