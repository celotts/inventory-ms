package com.celotts.purchaseservice.domain.exception;

public class ServiceUnavailableException extends BaseDomainException {
    public ServiceUnavailableException(String messageKey) {
        super(messageKey);
    }
}
