package com.celotts.supplierservice.domain.exception;

public class SupplierAlreadyExistsException extends BaseDomainException {

    public SupplierAlreadyExistsException(String field, String value) {
        super("supplier.already-exists", new Object[]{field, value});
    }
}
