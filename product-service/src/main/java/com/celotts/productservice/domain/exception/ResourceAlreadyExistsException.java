package com.celotts.productservice.domain.exception;

import com.celotts.productservice.infrastructure.common.error.ErrorCode;

public class ResourceAlreadyExistsException extends DomainException {
    public ResourceAlreadyExistsException(String resource, Object idOrKey) {
        super(ErrorCode.ALREADY_EXISTS, 409, resource + " already exists: " + idOrKey);
    }
}