package com.celotts.authservice.infrastructure.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private int code;
    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ResponseEntity<ApiResult<T>> toResponseEntity(T data, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ApiResult.<T>builder()
                        .code(status.value())
                        .status(status.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
