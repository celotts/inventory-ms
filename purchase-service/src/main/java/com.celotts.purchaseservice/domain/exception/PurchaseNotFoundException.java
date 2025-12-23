package com.celotts.purchaseservice.domain.exception;


public class PurchaseNotFoundException extends BaseDomainException {

    // Este constructor permite pasar el mensaje directamente al padre
    public PurchaseNotFoundException(String message) {
        super(message);
    }

    // Este permite pasar el mensaje y la causa del error
    public PurchaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Este permite usar el sistema de internacionalización (i18n) que creamos
    public PurchaseNotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
