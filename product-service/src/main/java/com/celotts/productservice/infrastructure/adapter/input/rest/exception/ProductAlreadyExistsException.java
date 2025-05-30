package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import java.util.UUID;

public class ProductAlreadyExistsException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado
     * @param message Mensaje de error personalizado
     */
    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor para producto que ya existe por ID
     * @param id UUID del producto que ya existe
     */
    public ProductAlreadyExistsException(UUID id) {
        super("Product already exists with id: " + id);
    }

    /**
     * Constructor para producto que ya existe por campo específico
     * @param field Nombre del campo (ejm: "code", "name")
     * @param value Valor del campo que ya existe
     */
    public ProductAlreadyExistsException(String field, String value) {
        super("Product already exists with " + field + ": " + value);
    }

    /**
     * Constructor con mensaje y causa
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor para ID con causa
     * @param id UUID del producto que ya existe
     * @param cause Causa de la excepción
     */
    public ProductAlreadyExistsException(UUID id, Throwable cause) {
        super("Product already exists with id: " + id, cause);
    }
}