package com.celotts.purchaseservice.domain.exception;

public class PurchaseAlreadyExistsException extends BaseDomainException {

    // Este constructor permite que el UseCase decida qué llave usar y qué argumentos pasar
    public PurchaseAlreadyExistsException(String messageKey, Object... args) {
        super(messageKey, args);
    }

    // Opcional: Por si necesitas pasar una causa técnica (error de base de datos, etc.)
    public PurchaseAlreadyExistsException(String messageKey, Throwable cause, Object... args) {
        super(messageKey, cause, args);
    }
}