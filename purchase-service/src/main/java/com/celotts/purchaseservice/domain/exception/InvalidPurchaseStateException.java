package com.celotts.purchaseservice.domain.exception;

public class InvalidPurchaseStateException extends BaseDomainException {
    public InvalidPurchaseStateException(String reason) {
        super(reason);
    }
}
