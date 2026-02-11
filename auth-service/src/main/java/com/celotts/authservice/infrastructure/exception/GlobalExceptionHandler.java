package com.celotts.authservice.infrastructure.exception;

import com.celotts.authservice.domain.exception.BaseAuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private String msg(String code, Object... args) {
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return code; // Fallback si no encuentra el mensaje
        }
    }

    @ExceptionHandler(BaseAuthException.class)
    public ProblemDetail handleBaseAuthException(BaseAuthException ex, HttpServletRequest req) {
        String detail = msg(ex.getMessageKey(), ex.getArgs());
        return buildProblemDetail(HttpStatus.BAD_REQUEST, msg("auth.error.title"), detail, "urn:celotts:error:auth-domain", req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, msg("auth.error.authentication.title"), msg("auth.error.bad-credentials"), "urn:celotts:error:bad-credentials", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildProblemDetail(HttpStatus.BAD_REQUEST, msg("auth.error.validation.title"), details, "urn:celotts:error:validation", req);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, msg("auth.error.internal.title"), ex.getMessage(), "urn:celotts:error:internal", req);
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String type, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create(type));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", req.getRequestURI());
        return pd;
    }
}
