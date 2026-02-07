package com.celotts.purchaseservice.infrastructure.adapter.input.rest.exception;

import com.celotts.purchaseservice.domain.exception.BaseDomainException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.exception.SupplierNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // 1. CAPTURA TUS ERRORES DE DOMINIO (404, 409, etc.)
    @ExceptionHandler(BaseDomainException.class)
    public ProblemDetail handleDomainException(BaseDomainException ex, Locale locale) {
        String message;
        try {
            if (ex.isI18n()) {
                message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), locale);
            } else {
                message = ex.getMessage();
            }
        } catch (Exception e) {
            String fallbackTitle = messageSource.getMessage("app.error.validation", null, locale);
            message = fallbackTitle + " (Key: " + ex.getMessageKey() + ") - Data: " + java.util.Arrays.toString(ex.getMessageArgs());
        }

        HttpStatus status = (ex instanceof PurchaseNotFoundException || ex instanceof SupplierNotFoundException)
                ? HttpStatus.NOT_FOUND
                : HttpStatus.CONFLICT;

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create("urn:celotts:error:" + ex.getMessageKey()));
        pd.setProperty("errorCode", ex.getMessageKey());
        pd.setProperty("timestamp", LocalDateTime.now());
        
        return pd;
    }

    // 2. CAPTURA ERRORES GENÉRICOS Y DE COMUNICACIÓN (500, 503)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, Locale locale) {
        String messageKey = ex.getMessage();
        String message;

        try {
            // Intentamos traducir "service.supplier.unavailable"
            message = messageSource.getMessage(messageKey, null, locale);
        } catch (Exception e) {
            // Si no es una llave, usamos el error inesperado genérico de tu properties
            String unexpected = messageSource.getMessage("app.error.unexpected", null, locale);
            message = unexpected + ": " + ex.getMessage();
        }

        // Si el mensaje contiene "unavailable", devolvemos 503. Si no, 500.
        HttpStatus status = (messageKey != null && messageKey.contains("unavailable"))
                ? HttpStatus.SERVICE_UNAVAILABLE
                : HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create("urn:celotts:error:internal"));
        pd.setProperty("errorCode", messageKey != null ? messageKey : "internal.error");
        pd.setProperty("timestamp", LocalDateTime.now());
        
        return pd;
    }
}