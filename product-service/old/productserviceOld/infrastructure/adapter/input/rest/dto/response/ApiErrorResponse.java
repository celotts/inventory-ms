package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
