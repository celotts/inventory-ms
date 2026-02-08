package com.celotts.purchaseservice.domain.exception;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public abstract class BaseDomainException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;
    private final Object[] messageArgs;

    protected BaseDomainException(String message) {
        super(message);
        this.messageKey = null;
        this.messageArgs = null;
    }

    protected BaseDomainException(String messageKey, boolean i18n, Object[] messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
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

}