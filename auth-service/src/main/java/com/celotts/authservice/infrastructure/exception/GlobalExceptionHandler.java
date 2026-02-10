package com.celotts.authservice.infrastructure.exception;

import com.celotts.authservice.domain.exception.EmailAlreadyExistsException;
import com.celotts.authservice.domain.exception.RoleNotFoundException;
import com.celotts.authservice.domain.exception.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ProblemDetail handleUsernameExists(UsernameAlreadyExistsException ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Username Taken", ex.getMessage(), "urn:celotts:error:username-taken", req);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Email Taken", ex.getMessage(), "urn:celotts:error:email-taken", req);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ProblemDetail handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, "Role Not Found", ex.getMessage(), "urn:celotts:error:role-not-found", req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Authentication Failed", "Invalid username or password", "urn:celotts:error:bad-credentials", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Validation Failed", details, "urn:celotts:error:validation", req);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), "urn:celotts:error:internal", req);
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
