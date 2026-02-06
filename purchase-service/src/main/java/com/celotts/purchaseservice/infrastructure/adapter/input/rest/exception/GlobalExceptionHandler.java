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
            message = ex.isI18n()
                    ? messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale)
                    : ex.getMessage();
        } catch (Exception e) {
            message = "Error: " + ex.getMessageKey();
        }

        // Determinamos el status
        HttpStatus status = (ex instanceof PurchaseNotFoundException || ex instanceof SupplierNotFoundException)
                ? HttpStatus.NOT_FOUND
                : HttpStatus.CONFLICT;

        // Armamos el JSON que SÍ queremos ver
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("code", ex.getMessageKey());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }
}