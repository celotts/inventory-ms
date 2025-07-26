package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testBuilderAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(now)
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .path("/api/products/123")
                .validationErrors(Map.of("field", "must not be null"))
                .build();

        assertEquals(now, response.getTimestamp());
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("Resource not found", response.getMessage());
        assertEquals("/api/products/123", response.getPath());
        assertNotNull(response.getValidationErrors());
        assertEquals("must not be null", response.getValidationErrors().get("field"));
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.of(2025, 7, 25, 15, 0));
        response.setStatus(400);
        response.setError("Bad Request");
        response.setMessage("Invalid input");
        response.setPath("/api/test");
        response.setValidationErrors(Map.of("name", "must not be blank"));

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getError());
        assertEquals("Invalid input", response.getMessage());
        assertEquals("/api/test", response.getPath());
        assertEquals("must not be blank", response.getValidationErrors().get("name"));
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse r1 = ErrorResponse.builder()
                .timestamp(now)
                .status(500)
                .error("Internal Error")
                .message("Unexpected error")
                .path("/api")
                .build();

        ErrorResponse r2 = ErrorResponse.builder()
                .timestamp(now)
                .status(500)
                .error("Internal Error")
                .message("Unexpected error")
                .path("/api")
                .build();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponse response = ErrorResponse.builder()
                .status(403)
                .error("Forbidden")
                .message("Access denied")
                .build();

        String string = response.toString();

        assertTrue(string.contains("403"));
        assertTrue(string.contains("Forbidden"));
        assertTrue(string.contains("Access denied"));
    }
}