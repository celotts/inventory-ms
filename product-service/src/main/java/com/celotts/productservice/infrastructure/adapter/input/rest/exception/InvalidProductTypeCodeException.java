package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

public class InvalidProductTypeCodeException extends RuntimeException {
    public InvalidProductTypeCodeException(String code) {
        super("El código de tipo de producto no es válido: " + code);
    }
}
