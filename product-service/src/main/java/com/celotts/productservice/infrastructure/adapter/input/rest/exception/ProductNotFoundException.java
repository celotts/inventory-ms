package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando un producto no es encontrado en el sistema
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado
     * Uso: new ProductNotFoundException("Product not found with code: " + code)
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor para producto no encontrado por ID
     * Uso: new ProductNotFoundException(uuid)
     */
    public ProductNotFoundException(UUID id) {
        super("Product not found with ID: " + id);
    }
}