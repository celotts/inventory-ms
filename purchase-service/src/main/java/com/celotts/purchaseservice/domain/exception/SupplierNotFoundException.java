package com.celotts.purchaseservice.domain.exception;

import lombok.Getter;

@Getter
public class SupplierNotFoundException extends BaseDomainException {
    private final String code;
    private final String field;
    private final String value;

    public SupplierNotFoundException(String code, String field, String value) {
        // Ahora este super llamará al constructor de BaseDomainException
        super(code, true, new Object[]{field, value});
        this.code = code;
        this.field = field;
        this.value = value;
    }
}