package com.celotts.supplierservice.domain.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class BaseDomainException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;
    private final Object[] messageArgs;
    private final boolean i18n;

    // Constructor para mensajes planos (sin i18n)
    protected BaseDomainException(String message) {
        super(message);
        this.messageKey = null;
        this.messageArgs = null;
        this.i18n = false;
    }

    // Constructor principal para i18n
    protected BaseDomainException(String messageKey, Object[] messageArgs) {
        super(messageKey); // El mensaje de la excepción será la clave, útil para logs
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
        this.i18n = true;
    }
}
