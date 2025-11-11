package com.celotts.supplierservice.domain.exception;

public class SupplierAlreadyExistsException extends BaseDomainException {
    public SupplierAlreadyExistsException(String field, String value) {
        super("supplier.already-exists", field, value);
    }
    public SupplierAlreadyExistsException(String field, String value, Throwable cause) {
        super("supplier.already-exists", cause, field, value);
    }
}