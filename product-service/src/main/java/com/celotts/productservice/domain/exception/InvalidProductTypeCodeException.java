package com.celotts.productservice.domain.exception;

public class InvalidProductTypeCodeException extends RuntimeException {
    public InvalidProductTypeCodeException(String code) {
        super("El código de tipo de producto no es válido: " + code);
    }
}
