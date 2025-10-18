package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.response;

import com.celotts.supplierservice.infrastructure.config.SpringMessageSourceHolder;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 🌐 Representa una respuesta estándar de error para la API.
 * Soporta internacionalización (i18n) usando MessageSource y LocaleContextHolder.
 */
public record ApiErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    // Constructor compacto: validación y normalización
    public ApiErrorResponse {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException(getLocalizedMessage("error.http-status.invalid", status));
        }

        error   = (error == null || error.isBlank())
                ? getLocalizedMessage("error.default.title")
                : error.strip();

        message = (message == null)
                ? getLocalizedMessage("error.default.message")
                : message.strip();

        path    = (path == null) ? "" : path;
    }

    // 🏗️ Fábrica básica: sin título personalizado
    public static ApiErrorResponse of(HttpStatus status, String message, String path) {
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                getLocalizedMessage("error." + status.name().toLowerCase(), status.getReasonPhrase()),
                message,
                path
        );
    }

    // 🏗️ Fábrica con título y mensaje traducibles
    public static ApiErrorResponse of(HttpStatus status, String errorKey, String messageKey, Object[] args, String path) {
        String localizedError = getLocalizedMessage(errorKey);
        String localizedMessage = getLocalizedMessage(messageKey, args);

        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                localizedError,
                localizedMessage,
                path
        );
    }

    // 🧠 Utilidad para obtener mensajes traducidos desde el MessageSource global
    private static String getLocalizedMessage(String key, Object... args) {
        try {
            MessageSource ms = SpringMessageSourceHolder.get();
            if (ms != null) {
                return ms.getMessage(key, args, LocaleContextHolder.getLocale());
            }
        } catch (Exception ignored) {}
        // fallback si no hay messageSource (por ejemplo en tests)
        return key;
    }
}