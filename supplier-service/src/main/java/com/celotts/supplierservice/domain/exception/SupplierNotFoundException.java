package com.celotts.supplierservice.domain.exception;

import java.util.UUID;

public class SupplierNotFoundException extends BaseDomainException {

    public SupplierNotFoundException(String field, String value) {
        // Pasamos: llave y los argumentos en un Object[]
        super("supplier.not-found", new Object[]{field, value});
    }

    public SupplierNotFoundException(UUID id) {
        super("supplier.not-found", new Object[]{"id", id.toString()});
    }
}
