package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.brand.BrandNotFoundException;
import com.celotts.productservice.domain.exception.product.ProductNotFoundException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Body malformado / JSON inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return ApiResponse.badRequest("Request body inválido o mal formado", path(req), method(req), null);
    }

    // 400 - @Valid en @RequestBody (DTO con BindingResult)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiResponse.Violation> violations = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> {
                    String field = (err instanceof FieldError fe) ? fe.getField() : null;
                    Object rejected = (err instanceof FieldError fe) ? fe.getRejectedValue() : null;
                    return new ApiResponse.Violation(field, err.getDefaultMessage(), rejected);
                })
                .collect(Collectors.toList());

        return ApiResponse.badRequest("Validación fallida", path(req), method(req), violations);
    }

    // 400 - @Validated en @RequestParam / @PathVariable (ConstraintViolation)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        List<ApiResponse.Violation> violations = ex.getConstraintViolations()
                .stream()
                .map(this::toViolation)
                .collect(Collectors.toList());

        return ApiResponse.badRequest("Parámetros inválidos", path(req), method(req), violations);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleBrandNotFound(BrandNotFoundException ex, HttpServletRequest req) {
        return ApiResponse.notFound(
                ex.getMessage(),
                path(req),
                method(req),
                "ERR_BRAND_NOT_FOUND"
        );
    }
    // 404 - Ejemplo con tus excepciones de dominio
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleProductNotFound(ProductNotFoundException ex, HttpServletRequest req) {
        return ApiResponse.notFound(ex.getMessage(), path(req), method(req), "ERR_PRODUCT_NOT_FOUND");
    }

    // 409 - Integridad de datos (FK, unique, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ApiResponse.conflict("Conflicto de integridad de datos", path(req), method(req), "ERR_DB_INTEGRITY");
    }

    // Errores “HTTP” ya construidos (opcional)
    @ExceptionHandler(ErrorResponseException.class)
    public ApiResponse<Void> handleErrorResponse(ErrorResponseException ex, HttpServletRequest req) {
        int status = ex.getStatusCode().value();
        String code = switch (status) {
            case 400 -> "ERR_BAD_REQUEST";
            case 403 -> "ERR_FORBIDDEN";
            case 404 -> "ERR_NOT_FOUND";
            default -> "ERR_HTTP";
        };
        return ApiResponse.error(status, ex.getBody().getDetail(), path(req), method(req), code, null);
    }

    // 500 - Fallback
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleUnexpected(Exception ex, HttpServletRequest req) {
        // En producción evita ex.getMessage() si puede filtrar detalles sensibles
        return ApiResponse.internal("Ocurrió un error inesperado", path(req), method(req), "ERR_INTERNAL");
    }

    // ----------------- Helpers -----------------
    private ApiResponse.Violation toViolation(ConstraintViolation<?> cv) {
        // propertyPath => p.ej. "create.dto.name"
        String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null;
        return new ApiResponse.Violation(field, cv.getMessage(), cv.getInvalidValue());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return ApiResponse.notFound(
                ex.getMessage(),
                path(req),
                method(req),
                "ERR_NOT_FOUND"
        );
    }

    private String path(HttpServletRequest req) { return req.getRequestURI(); }
    private String method(HttpServletRequest req) { return req.getMethod(); }
}