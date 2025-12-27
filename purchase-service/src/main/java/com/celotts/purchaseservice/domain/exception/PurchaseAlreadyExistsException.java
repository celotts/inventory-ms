package com.celotts.purchaseservice.domain.exception;

public class PurchaseAlreadyExistsException extends BaseDomainException {
    public PurchaseAlreadyExistsException(String field, String value ) {
        super("purchase.already-exists", field, value);
    }

    public PurchaseAlreadyExistsException(String field, String value, Throwable cause) {
        super("purchase.already-exist", cause, field, value);
    }
}
