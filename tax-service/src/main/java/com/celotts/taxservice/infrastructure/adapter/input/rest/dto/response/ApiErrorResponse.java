package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    @Autowired
    private static MessageSource messageSource;

    public ApiErrorResponse {
        if (status < 100 || status > 599) {
            var locale = LocaleContextHolder.getLocale();
            String msg = messageSource.getMessage(
                    "api.error.invalid.http.status",   // 🔑 clave del archivo messages_*.properties
                    new Object[]{ status },            // reemplaza {0} con el valor real
                    locale
            );
            throw new IllegalArgumentException(msg);
        }

        error   = (error == null || error.isBlank()) ? "Error" : error.strip();
        message = (message == null) ? "" : message.strip();
        path    = (path == null) ? "" : path;
    }
}