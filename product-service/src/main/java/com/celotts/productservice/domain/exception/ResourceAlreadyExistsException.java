package com.celotts.productservice.domain.exception;

import com.celotts.productservice.infrastructure.common.error.ErrorCode;

public class ResourceAlreadyExistsException extends DomainException {
    public ResourceAlreadyExistsException(String messageKey, Object... args) {
        super(ErrorCode.ALREADY_EXISTS, 409, messageKey, args);
    }
}