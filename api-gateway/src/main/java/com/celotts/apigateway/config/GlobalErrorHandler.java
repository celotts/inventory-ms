package com.celotts.apigateway.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {

    private final MessageSource messageSource;

    public GlobalErrorHandler(ErrorAttributes errorAttributes,
                              WebProperties webProperties,
                              ApplicationContext applicationContext,
                              ServerCodecConfigurer serverCodecConfigurer,
                              MessageSource messageSource) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
        this.messageSource = messageSource;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        HttpStatus status;
        String messageKey;
        String defaultMessage;
        String type;

        if (error instanceof AuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
            messageKey = "auth.error.unauthorized";
            defaultMessage = "Authentication required";
            type = "urn:celotts:error:unauthorized";
        } else if (error instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            messageKey = "auth.error.forbidden";
            defaultMessage = "Access denied";
            type = "urn:celotts:error:forbidden";
        } else if (error instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            messageKey = "error.http." + status.value();
            defaultMessage = rse.getReason() != null ? rse.getReason() : status.getReasonPhrase();
            type = "urn:celotts:error:http";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            messageKey = "error.internal";
            defaultMessage = "Unexpected error";
            type = "urn:celotts:error:internal";
        }

        String detail = getMsg(messageKey, defaultMessage);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create(type));
        pd.setProperty("timestamp", LocalDateTime.now());
        pd.setProperty("path", request.path());

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(pd));
    }

    private String getMsg(String key, String defaultMsg) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return defaultMsg;
        }
    }
}
