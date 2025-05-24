package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import java.util.UUID;

/**
 * Excepción lanzada cuando un producto no es encontrado en el sistema
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado
     * @param message Mensaje de error personalizado
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor para producto no encontrado por ID
     * @param id UUID del producto no encontrado
     */
    public ProductNotFoundException(UUID id) {
        super("Product not found with id: " + id);
    }

    /**
     * Constructor para producto no encontrado por campo específico
     * @param field Nombre del campo (ej: "code", "name")
     * @param value Valor del campo buscado
     */
    public ProductNotFoundException(String field, String value) {
        super("Product not found with " + field + ": " + value);
    }

    /**
     * Constructor con mensaje y causa
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor para ID con causa
     * @param id UUID del producto no encontrado
     * @param cause Causa de la excepción
     */
    public ProductNotFoundException(UUID id, Throwable cause) {
        super("Product not found with id: " + id, cause);
    }
}