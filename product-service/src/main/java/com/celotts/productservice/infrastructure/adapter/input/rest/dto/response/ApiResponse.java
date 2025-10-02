package com.celotts.productservice.infrastructure.adapter.input.rest.dto.response;

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
        Long total // opcional: útil cuando devuelves listas NO paginadas
) {
    // 200 OK con objeto
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "OK", Instant.now(), data, null);
    }

    // 201 Created con objeto
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Created", Instant.now(), data, null);
    }

    // 200 OK con lista NO paginada (calcula total automáticamente)
    public static <T> ApiResponse<List<T>> okList(List<T> list) {
        return new ApiResponse<>(200, "OK", Instant.now(), list, (long) list.size());
    }
}