package com.celotts.productservice.infrastructure.adapter.input.rest.exception.GlobalExceptionHandlerTest;

import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ErrorResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.GlobalExceptionHandler;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para GlobalExceptionHandler
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    void shouldHandle_ProductNotFoundException() {
        // Given
        UUID productId = UUID.randomUUID();
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleProductNotFoundException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Product Not Found", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("Product not found with id"));
        assertEquals("/api/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void shouldHandle_ProductAlreadyExistsException() {
        // Given
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException("code", "TEST123");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleProductAlreadyExistsException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(409, errorResponse.getStatus());
        assertEquals("Product Already Exists", errorResponse.getError());
        assertTrue(errorResponse.getMessage().contains("Product already exists with code: TEST123"));
        assertEquals("/api/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void shouldHandle_GenericException() {
        // Given
        Exception exception = new RuntimeException("Error genérico de prueba");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleGlobalException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }
}