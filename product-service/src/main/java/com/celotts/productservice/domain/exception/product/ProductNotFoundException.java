package com.celotts.productservice.domain.exception.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) { super(message); }
    public ProductNotFoundException(java.util.UUID id) {
        super("Producto no encontrado: " + id);
    }
}