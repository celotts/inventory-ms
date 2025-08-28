package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.application.name:product-service}")
    private String appName;

    // ---- 404
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ProblemDetail> notFound(ResourceNotFoundException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), "ERR_NOT_FOUND", req);
        log.warn("[{}] {} -> {}", appName, "ERR_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    // ---- 409
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    ResponseEntity<ProblemDetail> conflict(ResourceAlreadyExistsException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), "ERR_CONFLICT", req);
        log.warn("[{}] {} -> {}", appName, "ERR_CONFLICT", ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    // ---- 400 - Bean Validation en @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));

        var pd = problem(HttpStatus.BAD_REQUEST, "Validation Failed",
                "Los datos enviados no cumplen con las validaciones requeridas", "ERR_VALIDATION", req);
        pd.setProperty("validationErrors", errors);
        log.warn("[{}] {} -> {}", appName, "ERR_VALIDATION", errors);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 - JSON malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ProblemDetail> badJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.BAD_REQUEST, "Invalid JSON",
                "Error en el formato del JSON enviado", "ERR_JSON", req);
        var root = ex.getMostSpecificCause();
        if (root != null) pd.setProperty("cause", root.getMessage());
        log.warn("[{}] {} -> {}", appName, "ERR_JSON", root != null ? root.getMessage() : ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 - Integridad DB
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ProblemDetail> dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.BAD_REQUEST, "Data Integrity Violation",
                "Error de integridad en la base de datos", "ERR_DB_INTEGRITY", req);
        log.warn("[{}] {} -> {}", appName, "ERR_DB_INTEGRITY", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 - Tipo de argumento
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ProblemDetail> typeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String detail = "Parámetro inválido: " + ex.getName() +
                (ex.getValue() != null ? " = " + ex.getValue() : "");
        var pd = problem(HttpStatus.BAD_REQUEST, "Bad Request", detail, "ERR_PARAM_TYPE", req);
        log.warn("[{}] {} -> {}", appName, "ERR_PARAM_TYPE", detail);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 - Falta parámetro requerido
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ProblemDetail> missingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        String detail = "Falta el parámetro requerido: " + ex.getParameterName();
        var pd = problem(HttpStatus.BAD_REQUEST, "Bad Request", detail, "ERR_MISSING_PARAM", req);
        log.warn("[{}] {} -> {}", appName, "ERR_MISSING_PARAM", detail);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 - Violaciones de constraints en @RequestParam/@PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ProblemDetail> constraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var errors = ex.getConstraintViolations().stream()
                .collect(HashMap::new,
                        (m, v) -> m.put(v.getPropertyPath().toString(), v.getMessage()),
                        HashMap::putAll);
        var pd = problem(HttpStatus.BAD_REQUEST, "Constraint Violation",
                "Violaciones de validación en parámetros", "ERR_CONSTRAINT", req);
        pd.setProperty("validationErrors", errors);
        log.warn("[{}] {} -> {}", appName, "ERR_CONSTRAINT", errors);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 500 - Catch-all
    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> unexpected(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);
        var pd = problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ha ocurrido un error interno. Contacte al administrador.", "ERR_INTERNAL", req);
        return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---- Builder de ProblemDetail común
    private ProblemDetail problem(HttpStatus status, String title, String detail, String code, HttpServletRequest req) {
        var pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank"));                  // o una URI específica por código
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("service", appName);
        pd.setProperty("code", code);
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}