package com.celotts.supplierservice.domain.exception;

public class InvalidSupplierStateException extends BaseDomainException {
    public InvalidSupplierStateException(String reason) {
        super(reason);
    }
}
