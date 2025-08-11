package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/test-path");
    }

    // --- utils for MethodArgumentNotValidException ---
    private MethodParameter dummyMethodParameter() {
        try {
            Method m = this.getClass().getDeclaredMethod("dummyMethod", String.class);
            return new MethodParameter(m, 0);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unused")
    private void dummyMethod(String any) {}

    @Test
    @DisplayName("404 cuando el producto no existe")
    void handleProductNotFoundException() {
        ProductNotFoundException ex = mock(ProductNotFoundException.class);
        when(ex.getMessage()).thenReturn("Producto ABC no encontrado");

        ResponseEntity<ErrorResponse> response = handler.handleProductNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product Not Found", response.getBody().getError());
        assertEquals("Producto ABC no encontrado", response.getBody().getMessage());
        assertEquals("/test-path", response.getBody().getPath());
    }

    @Test
    @DisplayName("409 cuando el producto ya existe")
    void handleProductAlreadyExistsException() {
        ProductAlreadyExistsException ex = mock(ProductAlreadyExistsException.class);
        when(ex.getMessage()).thenReturn("Código duplicado PR-001");

        ResponseEntity<ErrorResponse> response = handler.handleProductAlreadyExistsException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Product Already Exists", response.getBody().getError());
        assertEquals("/test-path", response.getBody().getPath());
    }

    @Test
    @DisplayName("400 con mapa de errores cuando falla la validación")
    void handleValidationExceptions() throws Exception {
        Object target = new Object();
        BindingResult binding = new BeanPropertyBindingResult(target, "target");
        binding.addError(new FieldError("ProductCreateDto", "code", "no debe estar vacío"));
        binding.addError(new FieldError("ProductCreateDto", "name", "tamaño mínimo 3"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(dummyMethodParameter(), binding);

        ResponseEntity<ErrorResponse> response = handler.handleValidationExceptions(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Failed", response.getBody().getError());
        Map<String, String> errors = response.getBody().getValidationErrors();
        assertEquals("no debe estar vacío", errors.get("code"));
        assertEquals("tamaño mínimo 3", errors.get("name"));
        assertEquals("/test-path", response.getBody().getPath());
    }

    @Test
    @DisplayName("400 cuando JSON tiene formato inválido (InvalidFormatException)")
    void handleJsonParseError_invalidFormat() {
        InvalidFormatException cause = new InvalidFormatException(
                (JsonParser) null, "invalid format", "ABC", Integer.class);
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("JSON error", cause, null);

        ResponseEntity<ErrorResponse> response = handler.handleJsonParseError(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid JSON Format", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("formato inválido"));
    }

    @Test
    @DisplayName("400 cuando JSON tiene tipo incorrecto/campo faltante (MismatchedInputException)")
    void handleJsonParseError_mismatchedInput() {
        MismatchedInputException cause =
                MismatchedInputException.from((JsonParser) null, Integer.class, "bad type");
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("JSON error", cause, null);

        ResponseEntity<ErrorResponse> response = handler.handleJsonParseError(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid JSON Format", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("es requerido")
                || response.getBody().getMessage().contains("tipo de dato incorrecto"));
    }

    @Test
    @DisplayName("409 cuando hay violación de UNIQUE en código de producto")
    void handleDataIntegrityViolation_duplicateCode() {
        RuntimeException mostSpecific =
                new RuntimeException("duplicate key value violates unique constraint 'product_code_key'");
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("dup", mostSpecific);

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Ya existe un producto con el código especificado",
                response.getBody().getMessage());
    }

    @Test
    @DisplayName("400 cuando hay violación de FOREIGN KEY de brand")
    void handleDataIntegrityViolation_foreignKey_brand() {
        RuntimeException mostSpecific =
                new RuntimeException("insert or update on table \"product\" violates foreign key constraint \"fk_product_brand\": foreign key (brand_id) references brand(id)");
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("fk", mostSpecific);

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La marca especificada no existe en el sistema",
                response.getBody().getMessage());
    }

    @Test
    @DisplayName("400 cuando hay NOT NULL violation")
    void handleDataIntegrityViolation_notNull() {
        RuntimeException mostSpecific =
                new RuntimeException("null value in column \"name\" violates not null constraint");
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("nn", mostSpecific);

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(
                "Uno o más campos obligatorios están vacíos".equals(response.getBody().getMessage())
                        || "Uno o más datos de referencia no existen en el sistema".equals(response.getBody().getMessage()),
                "Mensaje inesperado: " + response.getBody().getMessage());
    }

    @Test
    @DisplayName("400 para IllegalArgumentException")
    void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");
        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgumentException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Argument", response.getBody().getError());
        assertEquals("Argumento inválido", response.getBody().getMessage());
    }

    @Test
    @DisplayName("500 para excepciones no controladas")
    void handleGlobalException() {
        Exception ex = new Exception("boom");
        ResponseEntity<ErrorResponse> response =
                handler.handleGlobalException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
        assertTrue(response.getBody().getTimestamp()
                .isBefore(LocalDateTime.now().plusSeconds(2)));
    }

    @Test
    @DisplayName("400 para InvalidProductTypeCodeException")
    void handleInvalidProductTypeCodeException() {
        InvalidProductTypeCodeException ex = mock(InvalidProductTypeCodeException.class);
        when(ex.getMessage()).thenReturn("tipo inválido");

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidProductTypeCodeException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Product Type Code", response.getBody().getError());
        assertEquals("/test-path", response.getBody().getPath());
    }

    @Test
    @DisplayName("400 para InvalidUnitCodeException")
    void handleInvalidUnitCodeException() {
        InvalidUnitCodeException ex = mock(InvalidUnitCodeException.class);
        when(ex.getMessage()).thenReturn("unidad inválida");

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidUnitCodeException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Unit Code", response.getBody().getError());
        assertEquals("/test-path", response.getBody().getPath());
    }

    @Test
    @DisplayName("400 para InvalidBrandIdException")
    void handleInvalidBrandIdException() {
        InvalidBrandIdException ex = mock(InvalidBrandIdException.class);
        when(ex.getMessage()).thenReturn("brand inválido");

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidBrandIdException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Brand ID", response.getBody().getError());
        assertEquals("/test-path", response.getBody().getPath());
    }
}