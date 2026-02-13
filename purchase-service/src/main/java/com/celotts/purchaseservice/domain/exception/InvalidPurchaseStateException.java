package com.celotts.purchaseservice.domain.exception;

public class InvalidPurchaseStateException extends BaseDomainException {
    public InvalidPurchaseStateException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
