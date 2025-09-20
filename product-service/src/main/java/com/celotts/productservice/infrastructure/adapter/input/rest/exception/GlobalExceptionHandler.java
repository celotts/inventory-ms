package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.domain.exception.BrandNotFoundException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.application.name:product-service}")
    private String appName;

    // ---- 404 (genérico de dominio)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(ResourceNotFoundException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), "ERR_NOT_FOUND", req);
        log.warn("[{}] {} -> {}", appName, "ERR_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    // ---- 404 (marca no encontrada)
    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ProblemDetail> brandNotFound(BrandNotFoundException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), "ERR_NOT_FOUND", req);
        log.warn("[{}] {} -> {}", appName, "ERR_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    // ---- 409 (recurso ya existe)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> conflict(ResourceAlreadyExistsException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), "ERR_CONFLICT", req);
        log.warn("[{}] {} -> {}", appName, "ERR_CONFLICT", ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    // ---- 409 (reglas de negocio simples, p.ej. nombre duplicado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> illegalArg(IllegalArgumentException ex, HttpServletRequest req) {
        var pd = problem(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), "ERR_CONFLICT", req);
        log.warn("[{}] {} -> {}", appName, "ERR_CONFLICT", ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    // ---- 409 (violaciones de integridad de BD: únicos, FK, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        var detail = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        var pd = problem(HttpStatus.CONFLICT, "Data Integrity Violation", "Error de integridad en la base de datos", "ERR_DB_INTEGRITY", req);
        pd.setProperty("cause", detail);
        log.warn("[{}] {} -> {}", appName, "ERR_DB_INTEGRITY", detail);
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    // ---- 400 (validación en @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));

        var pd = problem(HttpStatus.BAD_REQUEST, "Validation Failed",
                "Los datos enviados no cumplen con las validaciones requeridas", "ERR_VALIDATION", req);
        pd.setProperty("validationErrors", errors);
        log.warn("[{}] {} -> {}", appName, "ERR_VALIDATION", errors);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 (JSON malformado)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> badJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        var root = ex.getMostSpecificCause();
        var pd = problem(HttpStatus.BAD_REQUEST, "Invalid JSON", "Error en el formato del JSON enviado", "ERR_JSON", req);
        if (root != null) pd.setProperty("cause", root.getMessage());
        log.warn("[{}] {} -> {}", appName, "ERR_JSON", root != null ? root.getMessage() : ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 (tipo de argumento inválido)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> typeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String detail = "Parámetro inválido: " + ex.getName() +
                (ex.getValue() != null ? " = " + ex.getValue() : "");
        var pd = problem(HttpStatus.BAD_REQUEST, "Bad Request", detail, "ERR_PARAM_TYPE", req);
        log.warn("[{}] {} -> {}", appName, "ERR_PARAM_TYPE", detail);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 (falta parámetro requerido)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> missingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        String detail = "Falta el parámetro requerido: " + ex.getParameterName();
        var pd = problem(HttpStatus.BAD_REQUEST, "Bad Request", detail, "ERR_MISSING_PARAM", req);
        log.warn("[{}] {} -> {}", appName, "ERR_MISSING_PARAM", detail);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    // ---- 400 (violaciones de constraints en @RequestParam/@PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> constraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
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

    // ---- 405 (método no soportado)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> methodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                            HttpServletRequest req) {
        var pd = problem(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed",
                "Método HTTP no soportado para este endpoint", "ERR_METHOD_NOT_ALLOWED", req);
        pd.setProperty("method", ex.getMethod());
        pd.setProperty("supportedMethods", ex.getSupportedHttpMethods());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(pd);
    }

    // ---- 500 (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> unexpected(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);
        var pd = problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ha ocurrido un error interno. Contacte al administrador.", "ERR_INTERNAL", req);
        return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---- Builder común de ProblemDetail
    private ProblemDetail problem(HttpStatus status, String title, String detail, String code, HttpServletRequest req) {
        var pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank"));
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("service", appName);
        pd.setProperty("code", code);
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}