// domain/shared/DomainException.java
package com.celotts.productservice.domain.exception;

import com.celotts.productserviceOld.infrastructure.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final ErrorCode code;
    private final int httpStatus;

    public DomainException(ErrorCode code, int httpStatus, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }
}