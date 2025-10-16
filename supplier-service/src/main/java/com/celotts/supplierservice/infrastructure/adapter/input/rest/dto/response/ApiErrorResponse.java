package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Representa una respuesta estándar de error para la API.
 * Inmutable, segura y compatible con Spring Boot 3+ y Jackson.
 */
public record ApiErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    // Constructor compacto para validación o normalización
    public ApiErrorResponse {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("El código de estado HTTP no es válido: " + status);
        }
        error   = (error == null || error.isBlank()) ? "Error" : error.strip();
        message = (message == null) ? "" : message.strip();
        path    = (path == null) ? "" : path;
    }
}