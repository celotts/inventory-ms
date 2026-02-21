package com.celotts.purchaseservice.domain.exception;

import java.util.UUID;

public class TaxNotFoundException extends BaseDomainException {
    public TaxNotFoundException(String messageKey, UUID taxId) {
        super(messageKey, new Object[]{taxId});
    }
}
