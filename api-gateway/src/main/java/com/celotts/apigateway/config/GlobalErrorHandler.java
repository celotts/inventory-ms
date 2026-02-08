package com.celotts.apigateway.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

@Component
@Order(-2) // To give it higher priority than the default handler
@RequiredArgsConstructor
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Error handled in API Gateway: {}", ex.getMessage());

        Locale locale = exchange.getRequest().getHeaders().getAcceptLanguageAsLocales().stream()
                .findFirst()
                .orElse(Locale.US);

        ProblemDetail problemDetail;
        if (ex instanceof TimeoutException) {
            problemDetail = createProblemDetail(HttpStatus.GATEWAY_TIMEOUT, getMessage("error.gateway.timeout", locale), exchange);
        } else if (ex instanceof WebClientResponseException.ServiceUnavailable) {
            problemDetail = createProblemDetail(HttpStatus.SERVICE_UNAVAILABLE, getMessage("error.gateway.service-unavailable", locale), exchange);
        } else {
            problemDetail = createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("error.gateway.internal", locale), exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.valueOf(problemDetail.getStatus()));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(problemDetail);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            log.error("Error while serializing problem detail", e);
            return Mono.empty();
        }
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, ServerWebExchange exchange) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        pd.setType(URI.create("urn:celotts:error:gateway"));
        pd.setInstance(URI.create(exchange.getRequest().getPath().value()));
        pd.setProperty("timestamp", LocalDateTime.now());
        return pd;
    }

    private String getMessage(String key, Locale locale) {
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (Exception e) {
            log.warn("Message key '{}' not found for locale '{}'", key, locale);
            return key; // Fallback to the key itself
        }
    }
}
