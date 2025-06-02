package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    /**
     * Constructor principal - para mensajes personalizados
     * Uso: throw new ProductAlreadyExistsException("Ya existe un producto con el código: " + code);
     */
    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor estructurado - para casos específicos
     * Uso: throw new ProductAlreadyExistsException("code", productCode);
     * Ventaja: Mejor para logging y parsing automático
     */
    public ProductAlreadyExistsException(String field, String value) {
        super("Product already exists with " + field + ": " + value);
    }

    /**
     * Constructor con causa - solo si necesitas wrappear otras excepciones
     * Uso: En casos de DataIntegrityViolationException del JPA
     */
    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
