package com.celotts.purchaseservice.domain.exception;

import lombok.Getter;

@Getter
public class SupplierInactiveException extends RuntimeException {
    private final String code;
    private final String field;
    private final String value;

    public SupplierInactiveException(String code, String field, String value) {
        super(code);
        this.code = code;
        this.field = field;
        this.value = value;
    }
}