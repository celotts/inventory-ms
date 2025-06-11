package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    /**
     * Constructor principal - para mensajes personalizados
     * Uso: throw new ProductAlreadyExistsException("Ya existe un producto con el código: " + code);
     */
    public ProductAlreadyExistsException(String message) {
        super(message);
    }


}
