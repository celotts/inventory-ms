package com.celotts.supplierservice.domain.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class BaseDomainException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;     // p.ej. "supplier.already-exists"
    private final Object[] messageArgs;  // p.ej. { field, value }

    // Mensaje plano (legacy)
    protected BaseDomainException(String message) {
        super(message);
        this.messageKey = null;
        this.messageArgs = null;
    }

    protected BaseDomainException(String message, Throwable cause) {
        super(message, cause);
        this.messageKey = null;
        this.messageArgs = null;
    }

    // Clave i18n + args
    protected BaseDomainException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    protected BaseDomainException(String messageKey, Throwable cause, Object... messageArgs) {
        super(messageKey, cause);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public boolean isI18n() {
        return messageKey != null;
    }
}