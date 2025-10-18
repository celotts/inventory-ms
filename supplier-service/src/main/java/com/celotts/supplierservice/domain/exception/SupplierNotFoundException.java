package com.celotts.supplierservice.domain.exception;

import java.util.UUID;

public class SupplierNotFoundException extends BaseDomainException {

    public SupplierNotFoundException(String field, String value) {
        super("supplier.not-found", field, value);
    }

    // ✅ overload para UUID
    public SupplierNotFoundException(UUID id) {
        super("supplier.not-found", "id", id.toString());
    }
}