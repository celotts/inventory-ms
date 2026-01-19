package com.celotts.purchaseservice.domain.exception;


public class PurchaseNotFoundException extends BaseDomainException {

    public PurchaseNotFoundException(String message) {
        super(message);
    }

    public PurchaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PurchaseNotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
