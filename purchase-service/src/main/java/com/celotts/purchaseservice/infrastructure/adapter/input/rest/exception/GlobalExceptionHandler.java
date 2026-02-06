package com.celotts.purchaseservice.infrastructure.adapter.input.rest.exception;

import com.celotts.purchaseservice.domain.exception.BaseDomainException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.exception.SupplierNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap; // Para que el JSON salga ordenado
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice // Sin basePackages para evitar errores de escaneo
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(BaseDomainException ex, Locale locale) {
        Map<String, Object> response = new LinkedHashMap<>();

        String message;
        try {
            if (ex.isI18n()) {
                message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
            } else {
                message = ex.getMessage();
            }
        } catch (Exception e) {
            // ✅ Si la llave específica no existe, usamos una llave genérica de tus archivos
            // y le pegamos los datos técnicos para que el desarrollador sepa qué falló.
            String fallbackTitle = messageSource.getMessage("app.error.validation", null, locale);
            message = fallbackTitle + " (Key: " + ex.getMessageKey() + ") - Data: " + java.util.Arrays.toString(ex.getMessageArgs());
        }

        HttpStatus status = (ex instanceof PurchaseNotFoundException || ex instanceof SupplierNotFoundException)
                ? HttpStatus.NOT_FOUND
                : HttpStatus.CONFLICT;

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("code", ex.getMessageKey());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }
}