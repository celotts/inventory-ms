package com.celotts.productservice.domain.exception;

import com.celotts.productservice.infrastructure.common.error.ErrorCode;

public class ResourceNotFoundException extends DomainException {

    // Constructor para clave de mensaje y argumentos (estilo i18n)
    public ResourceNotFoundException(String messageKey, Object... args) {
        super(ErrorCode.NOT_FOUND, 404, messageKey); // Pasamos la clave como mensaje temporal
    }

    // Constructor legacy (para compatibilidad, pero marcado para evitarse)
    public ResourceNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, 404, message);
    }
}
