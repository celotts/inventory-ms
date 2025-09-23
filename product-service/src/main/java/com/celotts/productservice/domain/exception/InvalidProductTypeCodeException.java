package com.celotts.productservice.domain.exception;

public class InvalidProductTypeCodeException extends RuntimeException {
    public InvalidProductTypeCodeException(String code) {
        super("The product type code is not valid: " + code);
    }
}
