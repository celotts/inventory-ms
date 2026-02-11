package com.celotts.purchaseservice.domain.exception;

import java.util.UUID;

public class ProductNotFoundException extends BaseDomainException {
    public ProductNotFoundException(String messageKey, UUID productId) {
        super(messageKey, new Object[]{productId});
    }
}
