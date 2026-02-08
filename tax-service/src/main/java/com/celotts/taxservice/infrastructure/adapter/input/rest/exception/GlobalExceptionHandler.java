package com.celotts.taxservice.infrastructure.adapter.input.rest.exception;

import com.celotts.taxservice.domain.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler for tax-service.
 * Uses MessageSource for i18n messages.
 */
@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String msg(String key) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    // 400 - Malformed body / Invalid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg("error.badrequest.body"));
        pd.setTitle(msg("error.badrequest.title"));
        pd.setType(URI.create("urn:celotts:error:bad-request"));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 400 - @Valid on @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details);
        pd.setTitle(msg("error.validation.failed"));
        pd.setType(URI.create("urn:celotts:error:validation"));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 400 - @Validated (ConstraintViolation)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        String details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details);
        pd.setTitle(msg("error.constraint.invalid"));
        pd.setType(URI.create("urn:celotts:error:constraint"));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 409 - Data integrity (FK, unique, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, msg("error.db.conflict"));
        pd.setTitle(msg("data.integrity.title"));
        pd.setType(URI.create("urn:celotts:error:data-integrity"));
        pd.setProperty("errorCode", "DATA_INTEGRITY_VIOLATION");
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // Generic HTTP errors
    @ExceptionHandler(ErrorResponseException.class)
    public ProblemDetail handleErrorResponse(ErrorResponseException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String detail = ex.getBody().getDetail();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create("urn:celotts:error:http"));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 500 - Fallback
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, msg("error.internal"));
        pd.setTitle(msg("error.internal.title"));
        pd.setType(URI.create("urn:celotts:error:internal"));
        pd.setProperty("detail", ex.getMessage()); // Cuidado en prod
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle(msg("resource.not-found.title"));
        pd.setType(URI.create("urn:celotts:error:resource-not-found"));
        pd.setProperty("errorCode", "RESOURCE_NOT_FOUND");
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }
}