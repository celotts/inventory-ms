package com.celotts.purchaseservice.domain.exception;

public class PurchaseAlreadyExistsException extends BaseDomainException {

    // Constructor
    public PurchaseAlreadyExistsException(String messageKey, Object... args) {
        super(messageKey, args);
    }

    // Opcional: Por si se necesita pasar una causa técnica (error de base de datos, etc.)
    public PurchaseAlreadyExistsException(String messageKey, Throwable cause, Object... args) {
        super(messageKey, cause, args);
    }
}