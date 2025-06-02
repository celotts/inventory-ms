package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {

        log.warn("Product not found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Product Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex, WebRequest request) {

        log.warn("Product already exists: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Product Already Exists")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Validation error occurred: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
            log.debug("Validation error - Field: {}, Message: {}, Rejected value: {}",
                    fieldName, errorMessage, ((FieldError) error).getRejectedValue());
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Los datos enviados no cumplen con las validaciones requeridas")
                .path(request.getDescription(false).replace("uri=", ""))
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de formato JSON malformado o campos faltantes
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(
            HttpMessageNotReadableException ex, WebRequest request) {

        log.error("JSON parse error: {}", ex.getMessage());

        String userMessage = "Error en el formato del JSON enviado";

        // Detectar tipos específicos de errores JSON
        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
            String fieldName = invalidFormat.getPath().isEmpty() ? "unknown" :
                    invalidFormat.getPath().get(invalidFormat.getPath().size() - 1).getFieldName();
            userMessage = String.format("El campo '%s' tiene un formato inválido. Valor recibido: %s",
                    fieldName, invalidFormat.getValue());
        } else if (ex.getCause() instanceof MismatchedInputException mismatchedInput) {
            String fieldName = mismatchedInput.getPath().isEmpty() ? "unknown" :
                    mismatchedInput.getPath().get(mismatchedInput.getPath().size() - 1).getFieldName();
            userMessage = String.format("El campo '%s' es requerido pero no fue enviado o tiene el tipo de dato incorrecto", fieldName);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid JSON Format")
                .message(userMessage)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {

        log.error("Data integrity violation: {}", ex.getMessage());

        String customMessage = "Error de integridad en la base de datos";
        String rawMessage = ex.getMostSpecificCause().getMessage().toLowerCase();

        // Detectar tipos específicos de violaciones de integridad
        if (rawMessage.contains("duplicate") || rawMessage.contains("unique")) {
            if (rawMessage.contains("code")) {
                customMessage = "Ya existe un producto con el código especificado";
            } else {
                customMessage = "Ya existe un registro con los datos especificados";
            }
        } else if (rawMessage.contains("foreign key") || rawMessage.contains("constraint")) {
            if (rawMessage.contains("brand")) {
                customMessage = "La marca especificada no existe en el sistema";
            } else if (rawMessage.contains("product_type")) {
                customMessage = "El tipo de producto especificado no existe en el sistema";
            } else if (rawMessage.contains("unit")) {
                customMessage = "La unidad especificada no existe en el sistema";
            } else {
                customMessage = "Uno o más datos de referencia no existen en el sistema";
            }
        } else if (rawMessage.contains("not null")) {
            customMessage = "Uno o más campos obligatorios están vacíos";
        }

        // Usar CONFLICT para duplicados, BAD_REQUEST para otros errores de integridad
        HttpStatus status = (rawMessage.contains("duplicate") || rawMessage.contains("unique"))
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Data Integrity Violation")
                .message(customMessage)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Maneja argumentos ilegales (lógica de negocio)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        log.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Argument")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier otra excepción no capturada
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ha ocurrido un error interno. Por favor contacte al administrador del sistema")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}