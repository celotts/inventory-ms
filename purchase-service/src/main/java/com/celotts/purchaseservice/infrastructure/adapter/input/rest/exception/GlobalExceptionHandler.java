package com.celotts.purchaseservice.infrastructure.adapter.input.rest.exception;

import com.celotts.purchaseservice.domain.exception.BaseDomainException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.exception.SupplierNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException; // Importación necesaria

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
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

    // 2. CAPTURA ERRORES DE FORMATO JSON (400 Bad Request)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonErrors(HttpMessageNotReadableException ex, Locale locale) {
        String detail = "El cuerpo de la petición no es válido o tiene un formato incorrecto.";
        
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pd.setTitle("Bad Request");
        pd.setType(URI.create("urn:celotts:error:bad-request"));
        pd.setProperty("timestamp", LocalDateTime.now());

        if (ex.getCause() instanceof InvalidFormatException ifx) {
            String fieldName = ifx.getPath().isEmpty() ? "unknown" : ifx.getPath().get(0).getFieldName();
            String value = ifx.getValue().toString();
            String targetType = ifx.getTargetType().getSimpleName();
            
            String fieldMessage = String.format("El valor '%s' no es válido para el tipo '%s'.", value, targetType);
            if ("UUID".equals(targetType)) {
                fieldMessage = "El formato del ID no es válido (se espera un UUID).";
            }

            pd.setProperty("errors", List.of(
                Map.of(
                    "field", fieldName,
                    "message", fieldMessage,
                    "rejectedValue", value
                )
            ));
        }

        return pd;
    }

    // 3. CAPTURA RUTAS NO ENCONTRADAS (404) - NUEVO
    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(NoResourceFoundException ex, Locale locale) {
        String message = String.format("La ruta '%s' no existe en este servicio.", "/" + ex.getResourcePath());
        
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        pd.setTitle("Not Found");
        pd.setType(URI.create("urn:celotts:error:not-found"));
        pd.setProperty("errorCode", "route.not-found");
        pd.setProperty("timestamp", LocalDateTime.now());
        
        return pd;
    }

    // 4. CAPTURA ERRORES GENÉRICOS (500)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, Locale locale) {
        String messageKey = ex.getMessage();
        String message;

        try {
            message = messageSource.getMessage(messageKey, null, locale);
        } catch (Exception e) {
            String unexpected = messageSource.getMessage("app.error.unexpected", null, locale);
            message = unexpected + ": " + ex.getMessage();
        }

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
