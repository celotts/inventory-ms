package com.celotts.productservice.infrastructure.adapter.input.rest.exception;


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

import com.celotts.productserviceOld.domain.exception.ResourceNotFoundException;
import com.celotts.productserviceOld.domain.exception.ResourceAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.application.name:product-service}")
    private String appName;

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ProblemDetail> notFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), "ERR_NOT_FOUND", req);
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    ResponseEntity<ProblemDetail> conflict(ResourceAlreadyExistsException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), "ERR_CONFLICT", req);
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            errors.put(field, err.getDefaultMessage());
        });
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Validation Failed",
                "Los datos enviados no cumplen con las validaciones requeridas", "ERR_VALIDATION", req);
        pd.setProperty("validationErrors", errors);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ProblemDetail> badJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Invalid JSON",
                "Error en el formato del JSON enviado", "ERR_JSON", req);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ProblemDetail> dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Data Integrity Violation",
                "Error de integridad en la base de datos", "ERR_DB_INTEGRITY", req);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> illegalArg(IllegalArgumentException ex, HttpServletRequest req) {
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Invalid Argument", ex.getMessage(), "ERR_ARGUMENT", req);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> unexpected(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);
        ProblemDetail pd = problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ha ocurrido un error interno. Contacte al administrador.", "ERR_INTERNAL", req);
        return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, String code, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("service", appName);
        pd.setProperty("code", code);
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ProblemDetail> typeMismatch(MethodArgumentTypeMismatchException ex,
                                               HttpServletRequest req) {
        String detail = "Parámetro inválido: " + ex.getName();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        pd.setTitle("Bad Request");
        pd.setProperty("service", appName);
        pd.setProperty("code", "ERR_PARAM_TYPE");
        pd.setProperty("path", req.getRequestURI());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }
}