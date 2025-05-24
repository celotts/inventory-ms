package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    // Constructor específico para código
    public static ProductAlreadyExistsException forCode(String code) {
        return new ProductAlreadyExistsException("Product already exists with code: " + code);
    }

    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}