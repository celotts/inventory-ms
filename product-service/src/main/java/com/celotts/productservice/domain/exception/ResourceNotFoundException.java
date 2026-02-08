package com.celotts.productservice.domain.exception;

import com.celotts.productservice.infrastructure.common.error.ErrorCode;

public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String resource, Object idOrKey) {
        super(ErrorCode.NOT_FOUND, 404, resource + " not found with id: " + idOrKey);
    }

    public ResourceNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, 404, message);
    }

    public ResourceNotFoundException(ErrorCode code, String message) {
        super(code, 404, message);
    }
}