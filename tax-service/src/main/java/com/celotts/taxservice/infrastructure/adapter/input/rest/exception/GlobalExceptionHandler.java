package com.celotts.taxservice.infrastructure.adapter.input.rest.exception;

import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

/**
 * Global exception handler for tax-service.
 * Uses MessageSource for i18n messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String msg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    // 400 - Malformed body / Invalid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return ApiResponse.badRequest(
                msg("error.badrequest.body"),
                path(req),
                method(req),
                null
        );
    }

    // 400 - @Valid on @RequestBody
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

        return ApiResponse.badRequest(
                msg("error.validation.failed"),
                path(req),
                method(req),
                violations
        );
    }

    // 400 - @Validated (ConstraintViolation)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        List<ApiResponse.Violation> violations = ex.getConstraintViolations()
                .stream()
                .map(this::toViolation)
                .collect(Collectors.toList());

        return ApiResponse.badRequest(
                msg("error.constraint.invalid"),
                path(req),
                method(req),
                violations
        );
    }

    // 409 - Data integrity (FK, unique, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ApiResponse.conflict(
                msg("error.db.conflict"),
                path(req),
                method(req),
                "ERR_DB_INTEGRITY"
        );
    }

    // Generic HTTP errors
    @ExceptionHandler(ErrorResponseException.class)
    public ApiResponse<Void> handleErrorResponse(ErrorResponseException ex, HttpServletRequest req) {
        int status = ex.getStatusCode().value();
        String code = switch (status) {
            case 400 -> "ERR_BAD_REQUEST";
            case 403 -> "ERR_FORBIDDEN";
            case 404 -> "ERR_NOT_FOUND";
            default -> "ERR_HTTP";
        };
        String detail = ex.getBody() != null ? ex.getBody().getDetail() : msg("error.http.invalid");
        return ApiResponse.error(
                status,
                detail,
                path(req),
                method(req),
                code,
                null
        );
    }

    // 500 - Fallback
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleUnexpected(Exception ex, HttpServletRequest req) {
        return ApiResponse.internal(
                msg("error.internal"),
                path(req),
                method(req),
                "ERR_INTERNAL"
        );
    }

    // Optional: ResourceNotFoundException handler (uncomment / adjust import if you have this exception in tax-service)
    /*
    @ExceptionHandler(com.celotts.taxservice.domain.exception.ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFound(com.celotts.taxservice.domain.exception.ResourceNotFoundException ex, HttpServletRequest req) {
        return ApiResponse.notFound(
                ex.getMessage(),
                path(req),
                method(req),
                "ERR_NOT_FOUND"
        );
    }
    */

    // ----------------- Helpers -----------------
    private ApiResponse.Violation toViolation(ConstraintViolation<?> cv) {
        String field = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null;
        return new ApiResponse.Violation(field, cv.getMessage(), cv.getInvalidValue());
    }

    private String path(HttpServletRequest req) {
        return req == null ? null : req.getRequestURI();
    }

    private String method(HttpServletRequest req) {
        return req == null ? null : req.getMethod();
    }
}