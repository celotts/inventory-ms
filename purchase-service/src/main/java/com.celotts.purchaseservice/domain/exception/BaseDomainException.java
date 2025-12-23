package com.celotts.purchaseservice.domain.exception;

import java.io.Serializable;

public abstract class BaseDomainException extends RuntimeException implements Serializable {

    // Eliminamos @Serial porque tu entorno no reconoce el símbolo
    private static final long serialVersionUID = 1L;

    private final String messageKey;
    private final Object[] messageArgs;

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

    protected BaseDomainException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    protected BaseDomainException(String messageKey, Throwable cause, Object... messageArgs){
        super(messageKey, cause);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public boolean isI18n() {
        return messageKey != null;
    }

    public String getMessageKey() { return messageKey; }
    public Object[] getMessageArgs() { return messageArgs; }
}