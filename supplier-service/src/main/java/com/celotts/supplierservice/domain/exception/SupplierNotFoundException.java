package com.celotts.supplierservice.domain.exception;

import java.util.UUID;

public class SupplierNotFoundException extends BaseDomainException {

    public SupplierNotFoundException(String field, String value) {
        // Pasamos: llave, si es i18n, y los argumentos en un Object[]
        super("supplier.not-found", true, new Object[]{field, value});
    }

    public SupplierNotFoundException(UUID id) {
        super("supplier.not-found", true, new Object[]{"id", id.toString()});
    }
}