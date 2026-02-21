package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.DomainException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.exception.brand.BrandNotFoundException;
import com.celotts.productservice.domain.exception.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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
import java.util.List;
import java.util.Map;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private String msg(String key, Object[] args) {
        if (key == null) return null;
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return key;
        }
    }

    private String msg(String key) {
        return msg(key, null);
    }

    // Método Helper para centralizar la creación de respuestas (DRY)
    private ProblemDetail buildProblemDetail(HttpStatus status, String detail, String titleKey, String typeSuffix, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(msg(titleKey));
        pd.setType(URI.create("urn:celotts:error:" + typeSuffix));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    // 400 - Body malformado / JSON inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, msg("error.badrequest.body"), "error.badrequest.title", "bad-request", req);
    }

    // 400 - @Valid en @RequestBody (DTO con BindingResult)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage() != null ? fe.getDefaultMessage() : msg("error.validation.invalid")
                ))
                .toList();

        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST, msg("error.validation.detail"), "error.validation.failed", "validation", req);
        pd.setProperty("errors", errors);
        return pd;
    }

    // 400 - @Validated en @RequestParam / @PathVariable (ConstraintViolation)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        // Extraer solo el nombre del parámetro (ej. "id") eliminando el nombre del método (ej. "getProduct.id")
                        "parameter", extractPropertyName(v.getPropertyPath().toString()),
                        "message", v.getMessage()
                ))
                .toList();

        ProblemDetail pd = buildProblemDetail(HttpStatus.BAD_REQUEST, msg("error.constraint.detail"), "error.constraint.invalid", "constraint", req);
        pd.setProperty("errors", errors);
        return pd;
    }

    // Helper para limpiar el nombre de la propiedad en validaciones
    private String extractPropertyName(String path) {
        int lastDot = path.lastIndexOf('.');
        return lastDot != -1 ? path.substring(lastDot + 1) : path;
    }

    // Manejo de excepciones de dominio genéricas
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getHttpStatus());
        String typeSuffix = ex.getCode() != null ? ex.getCode().name().toLowerCase() : "domain";
        
        String detail = msg(ex.getMessage(), ex.getArgs());
        
        ProblemDetail pd = buildProblemDetail(status, detail, null, typeSuffix, req);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("errorCode", ex.getCode());
        return pd;
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ProblemDetail handleBrandNotFound(BrandNotFoundException ex, HttpServletRequest req) {
        String detail = msg(ex.getMessage(), ex.getArgs());
        ProblemDetail pd = buildProblemDetail(HttpStatus.NOT_FOUND, detail, "brand.not-found.title", "brand-not-found", req);
        pd.setProperty("errorCode", "BRAND_NOT_FOUND");
        return pd;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        String detail = msg(ex.getMessage(), ex.getArgs());
        ProblemDetail pd = buildProblemDetail(HttpStatus.NOT_FOUND, detail, "product.not-found.title", "product-not-found", req);
        pd.setProperty("errorCode", "PRODUCT_NOT_FOUND");
        return pd;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        String detail = msg(ex.getMessage(), ex.getArgs());
        ProblemDetail pd = buildProblemDetail(HttpStatus.NOT_FOUND, detail, "resource.not-found.title", "resource-not-found", req);
        pd.setProperty("errorCode", "RESOURCE_NOT_FOUND");
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = buildProblemDetail(HttpStatus.CONFLICT, msg("error.db.conflict"), "data.integrity.title", "data-integrity", req);
        pd.setProperty("errorCode", "DATA_INTEGRITY_VIOLATION");
        return pd;
    }

    // 500 - Fallback
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("Error inesperado procesando petición en: {}", req.getRequestURI(), ex);

        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, msg("error.internal"), "error.internal.title", "internal", req);
    }
}