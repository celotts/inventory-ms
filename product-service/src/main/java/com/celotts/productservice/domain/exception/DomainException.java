package com.celotts.productservice.domain.exception;

import com.celotts.productservice.infrastructure.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final ErrorCode code;
    private final int httpStatus;
    private final Object[] args;

    public DomainException(ErrorCode code, int httpStatus, String message, Object... args) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.args = args;
    }
}