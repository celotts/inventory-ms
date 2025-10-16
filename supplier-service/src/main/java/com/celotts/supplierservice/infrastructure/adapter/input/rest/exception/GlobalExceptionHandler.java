package com.celotts.supplierservice.infrastructure.adapter.input.rest.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String detail = toFriendlyMessage(ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid or malformed request body");
        pd.setDetail(detail != null ? detail : "The provided JSON is invalid or does not match the expected format.");
        pd.setType(URI.create("about:blank")); // o tu URL de documentación
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("method", req.getMethod());
        return pd;
    }

    // -------------------- helpers --------------------

    private String toFriendlyMessage(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getRootCause() != null ? ex.getRootCause() : ex.getCause();

        if (cause instanceof UnrecognizedPropertyException e) {
            String known = (e.getKnownPropertyIds() != null && !e.getKnownPropertyIds().isEmpty())
                    ? " Allowed properties: " + e.getKnownPropertyIds()
                    : "";
            return "Unrecognized property '" + e.getPropertyName() + "' at " + atPath(e) + "." + known;
        }

        if (cause instanceof PropertyBindingException e) {
            return "Invalid property binding at " + atPath(e) + ": " + e.getOriginalMessage();
        }

        if (cause instanceof InvalidFormatException e) {
            String target = e.getTargetType() != null ? e.getTargetType().getSimpleName() : "target type";
            String value  = e.getValue() != null ? String.valueOf(e.getValue()) : "null";
            String path   = atPath(e);
            return "Invalid value '" + value + "' for " + target + " at " + path + ". " +
                    "Make sure the value matches the expected format/type.";
        }

        if (cause instanceof MismatchedInputException e) {
            String target = e.getTargetType() != null ? e.getTargetType().getSimpleName() : "structure";
            return "Malformed request body. Mismatched input at " + atPath(e) +
                    " (expected " + target + ").";
        }

        if (cause instanceof JsonParseException e) {
            return "Malformed JSON at line " + e.getLocation().getLineNr() +
                    ", column " + e.getLocation().getColumnNr() + ".";
        }

        return cause != null ? cause.getMessage() : null;
    }

    private String atPath(JsonMappingException e) {
        if (e.getPath() == null || e.getPath().isEmpty()) return "$";
        return "$." + e.getPath().stream()
                .map(this::segment)
                .collect(Collectors.joining(".")); // $.items[0].quantity
    }

    private String segment(Reference ref) {
        if (ref.getFieldName() != null) return ref.getFieldName();
        if (ref.getIndex() >= 0) return "[" + ref.getIndex() + "]";
        return "?";
    }
}