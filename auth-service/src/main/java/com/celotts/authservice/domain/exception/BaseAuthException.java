package com.celotts.authservice.domain.exception;

import lombok.Getter;

@Getter
public abstract class BaseAuthException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public BaseAuthException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }
}
