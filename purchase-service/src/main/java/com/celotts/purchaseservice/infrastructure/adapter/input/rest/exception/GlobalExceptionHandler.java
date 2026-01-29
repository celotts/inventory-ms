package com.celotts.purchaseservice.infrastructure.adapter.input.rest.exception;

import com.celotts.purchaseservice.domain.exception.BaseDomainException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.celotts.purchaseservice.infrastructure.adapter.input.rest")
@RequiredArgsConstructor // Para inyectar MessageSource
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // CAPTURA TODAS LAS EXCEPCIONES DE DOMINIO (NotFound, AlreadyExists, etc.)
    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(BaseDomainException ex, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());

        String message;
        try {
            if (ex.isI18n()) {
                message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
            } else {
                message = ex.getMessage();
            }
        } catch (Exception e) {
            message = "Error code: " + ex.getMessageKey();
        }

        response.put("message", message);

        HttpStatus status = (ex instanceof PurchaseNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;
        response.put("status", status.value());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}