package com.celotts.taxservice.infrastructure.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SuppressWarnings("unused")
@Getter
@Builder
public class ApiResponse<T> {
    private final boolean success;
    private final int statusCode;
    private final String message;

    private final T data;

    private final String path;
    private final String method;
    private final String code; // Código de error interno (e.g., ERR_NOT_FOUND)
    private final List<Violation> violations; // Lista de errores de validación
    private ApiResponse(boolean success, int statusCode, String message, T data, String path, String method, String code, List<Violation> violations) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.path = path;
        this.method = method;
        this.code = code;
        this.violations = violations;
    }

    @Getter
    @AllArgsConstructor
    public static class Violation {
        private final String field;
        private final String message;
        private final Object rejectedValue;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status, String message) {

        @SuppressWarnings("unchecked")
        ApiResponse<T> response = (ApiResponse<T>) ApiResponse.builder() // <--- ¡Haz el cast aquí!
                .success(true)
                .statusCode(status.value())
                .message(message)
                .data(data)
                .path(null)
                .method(null)
                .code(null)
                .violations(null)
                .build();

        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse<Void>> error(String message, HttpStatus status) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .statusCode(status.value())
                .message(message)
                .data(null)
                .path(null)
                .method(null)
                .code(null)
                .violations(null)
                .build();
        return new ResponseEntity<>(response, status);
    }



}
