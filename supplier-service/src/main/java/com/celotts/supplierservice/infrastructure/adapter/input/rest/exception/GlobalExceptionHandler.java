package com.celotts.supplierservice.infrastructure.adapter.input.rest.exception;

import com.celotts.supplierservice.domain.exception.SupplierAlreadyExistsException;
import com.celotts.supplierservice.domain.exception.SupplierNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    // helper i18n
    private String msg(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    // -------------------- JSON mal formado --------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String detail = toFriendlyMessage(ex);
        return problem(
                HttpStatus.BAD_REQUEST,
                msg("json.invalid.title"),
                (detail != null ? detail : msg("json.invalid.detail")),
                req.getRequestURI(),
                "https://api.celotts.com/errors/json-parse"
        );
    }

    // -------------------- Validación: @Valid (body) --------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return problem(
                HttpStatus.BAD_REQUEST,
                msg("validation.failed.title"),
                details,
                req.getRequestURI(),
                "https://api.celotts.com/errors/validation"
        );
    }

    // -------------------- Validación: @Validated en params/path --------------------
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        String details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return problem(
                HttpStatus.BAD_REQUEST,
                msg("validation.failed.title"),
                details,
                req.getRequestURI(),
                "https://api.celotts.com/errors/validation"
        );
    }

    // -------------------- Bind errors (query/form sin @Valid body) --------------------
    @ExceptionHandler(BindException.class)
    public ProblemDetail handleBind(BindException ex, HttpServletRequest req) {
        String details = ex.getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return problem(
                HttpStatus.BAD_REQUEST,
                msg("validation.failed.title"),
                details,
                req.getRequestURI(),
                "https://api.celotts.com/errors/validation"
        );
    }

    // -------------------- Falta parámetro obligatorio --------------------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return problem(
                HttpStatus.BAD_REQUEST,
                msg("validation.failed.title"),
                msg("validation.missing-param", ex.getParameterName()),
                req.getRequestURI(),
                "https://api.celotts.com/errors/missing-param"
        );
    }

    // -------------------- Dominio: Not Found --------------------
    @ExceptionHandler(SupplierNotFoundException.class)
    public ProblemDetail handleSupplierNotFound(SupplierNotFoundException ex, WebRequest req) {
        String titleAndDetail = ex.isI18n()
                ? messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale())
                : ex.getMessage();

        return problem(
                HttpStatus.NOT_FOUND,
                msg("error.not-found.title"),
                titleAndDetail,
                path(req),
                "https://api.celotts.com/errors/supplier-not-found"
        );
    }

    // -------------------- Dominio: Conflicto --------------------
    @ExceptionHandler(SupplierAlreadyExistsException.class)
    public ProblemDetail handleSupplierExists(SupplierAlreadyExistsException ex, WebRequest req) {
        String titleAndDetail = ex.isI18n()
                ? messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale())
                : ex.getMessage();

        return problem(
                HttpStatus.CONFLICT,
                msg("error.default.title"),
                titleAndDetail,
                path(req),
                "https://api.celotts.com/errors/supplier-conflict"
        );
    }

    // -------------------- Conflictos de BD (únicos/foreign, etc.) --------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return problem(
                HttpStatus.CONFLICT,
                msg("data.integrity.title"),
                msg("data.integrity.detail"),
                req.getRequestURI(),
                "https://api.celotts.com/errors/data-integrity"
        );
    }

    // -------------------- HTTP 405 / 415 --------------------
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return problem(
                HttpStatus.METHOD_NOT_ALLOWED,
                msg("method.not.allowed.title"),
                msg("method.not.allowed.detail", ex.getMethod()),
                req.getRequestURI(),
                "https://api.celotts.com/errors/method-not-allowed"
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return problem(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                msg("media.type.unsupported.title"),
                msg("media.type.unsupported.detail", ex.getContentType()),
                req.getRequestURI(),
                "https://api.celotts.com/errors/unsupported-media-type"
        );
    }

    // -------------------- Catch-all (500) --------------------
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        return problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                msg("error.internal.title"),
                msg("error.internal.detail", ex.getMessage()),
                req.getRequestURI(),
                "https://api.celotts.com/errors/internal"
        );
    }

    // -------------------- helpers --------------------
    private ProblemDetail problem(HttpStatus status, String title, String detail, String path, String typeUrl) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create(typeUrl));
        pd.setProperty("path", path);
        pd.setProperty("timestamp", LocalDateTime.now());
        return pd;
    }

    private String path(WebRequest req) {
        return req.getDescription(false).replace("uri=", "");
    }

    private String toFriendlyMessage(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getRootCause() != null ? ex.getRootCause() : ex.getCause();

        if (cause instanceof UnrecognizedPropertyException e) {
            String known = (e.getKnownPropertyIds() != null && !e.getKnownPropertyIds().isEmpty())
                    ? msg("json.unrecognized.known", e.getKnownPropertyIds())
                    : "";
            return msg("json.unrecognized.property",
                    e.getPropertyName(),
                    atPath(e),
                    known);
        }
        if (cause instanceof PropertyBindingException e) {
            return msg("json.property.binding", atPath(e), e.getOriginalMessage());
        }
        if (cause instanceof InvalidFormatException e) {
            String target = (e.getTargetType() != null ? e.getTargetType().getSimpleName() : msg("json.target.type.unknown"));
            String value  = (e.getValue() != null ? String.valueOf(e.getValue()) : "null");
            return msg("json.invalid.format", value, target, atPath(e));
        }
        if (cause instanceof MismatchedInputException e) {
            String target = (e.getTargetType() != null ? e.getTargetType().getSimpleName() : msg("json.target.type.unknown"));
            return msg("json.mismatched.input", atPath(e), target);
        }
        if (cause instanceof JsonParseException e) {
            return msg("json.malformed.at", e.getLocation().getLineNr(), e.getLocation().getColumnNr());
        }
        return (cause != null ? cause.getMessage() : null);
    }

    private String atPath(JsonMappingException e) {
        if (e.getPath() == null || e.getPath().isEmpty()) return "$";
        return "$." + e.getPath().stream().map(this::segment).collect(Collectors.joining("."));
    }

    private String segment(Reference ref) {
        if (ref.getFieldName() != null) return ref.getFieldName();
        if (ref.getIndex() >= 0) return "[" + ref.getIndex() + "]";
        return "?";
    }
}