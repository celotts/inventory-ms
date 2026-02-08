package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.DomainException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.exception.brand.BrandNotFoundException;
import com.celotts.productservice.domain.exception.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    // 400 - Body malformado / JSON inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg("error.badrequest.body"));
        pd.setTitle(msg("error.badrequest.title"));
        pd.setType(URI.create("urn:celotts:error:bad-request"));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 400 - @Valid en @RequestBody (DTO con BindingResult)
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

    // 400 - @Validated en @RequestParam / @PathVariable (ConstraintViolation)
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

    // Manejo de excepciones de dominio genéricas
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getHttpStatus());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create("urn:celotts:error:" + (ex.getCode() != null ? ex.getCode().name().toLowerCase() : "domain")));
        pd.setProperty("errorCode", ex.getCode());
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ProblemDetail handleBrandNotFound(BrandNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle(msg("brand.not-found.title"));
        pd.setType(URI.create("urn:celotts:error:brand-not-found"));
        pd.setProperty("errorCode", "BRAND_NOT_FOUND");
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle(msg("product.not-found.title"));
        pd.setType(URI.create("urn:celotts:error:product-not-found"));
        pd.setProperty("errorCode", "PRODUCT_NOT_FOUND");
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

    // 409 - Integridad de datos (FK, unique, etc.)
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
}