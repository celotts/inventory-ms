package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    int status,
    String message,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    Instant timestamp,
    T data,
    Long total,
    String path,
    String method,
    String errorCode,
    List<Violation> errors
){
    // ----------------- ÉXITO -----------------
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "OK", Instant.now(), data, null, null, null, null, null);
    }

    public static <T> ApiResponse<T> created (T data) {
        return new ApiResponse<>(201, "Created", Instant.now(), data, null, null, null, null, null);
    }

    public static <T> ApiResponse<List<T>> okList(List<T> list) {
        return new ApiResponse<>(200, "OK", Instant.now(),list, (long) list.size(), null, null, null, null);
    }
    // ----------------- ERROR -----------------
    public static <T> ApiResponse<T> error (
        int status, String message,
        String path, String method,
        String errorCode, List<Violation> errors
    ) {
        return new ApiResponse<>(status, message, Instant.now(), null, null, path, method, errorCode, errors );
    }

    public static <T> ApiResponse<T>badRequest(String message, String path, String method, List<Violation> errors){
        return error(400, message, path, method, "ERROR_BAD_REQUEST", errors);
    }

    public static <T> ApiResponse<T> notFound(String message, String path,  String method, String errorCode) {
        return error(404,  message, path, method, errorCode != null ? errorCode : "ERR_NOT_FOUND", null);
    }

    public static <T> ApiResponse<T> conflict(String message, String path, String method, String errorCode) {
        return error(409, message, path, method, errorCode != null ? errorCode: "ERR_CONFLICT", null );
    }

    public static <T> ApiResponse<T> forbidden(String message, String path, String method) {
        return error(403, message, path, method, "ERR_FORBIDDEN", null);
    }

    public static<T> ApiResponse<T> internal(String message, String path, String method, String errorCode) {
        return error(500, message, path, method,errorCode != null ? errorCode : "ERR_INTERNAL", null);
    }
    // ----------------- DTO de violaciones -----------------
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Violation(
            String field,       // "name", "price", etc. (puede ser null para errores globales)
            String message,     // mensaje legible
            Object rejectedValue // valor rechazado (opcional)
    ) { }
}
