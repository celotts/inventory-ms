package com.celotts.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    // Rutas que no requieren autenticación
    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/auth-service/api/auth/login",
            "/auth-service/api/auth/register",
            "/eureka"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Verificar si la ruta requiere autenticación
        if (isSecured.test(request)) {
            // Verificar si el header Authorization está presente
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }

            final String authorizationHeader = request.getHeaders().getOrEmpty("Authorization").get(0);

            String username = null;
            String jwt = null;

            // Verificar formato del token
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    username = jwtUtil.extractUsername(jwt);
                } catch (Exception e) {
                    return onError(exchange, "JWT token is invalid", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return onError(exchange, "JWT token does not begin with Bearer", HttpStatus.UNAUTHORIZED);
            }

            // Validar el token
            if (username != null) {
                if (jwtUtil.isTokenValid(jwt, username)) {
                    // Token válido, agregar información del usuario al request
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Name", username)
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } else {
                    return onError(exchange, "JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
                }
            }
        }

        return chain.filter(exchange);
    }

    // Predicate para verificar si la ruta está asegurada
    public Predicate<ServerHttpRequest> isSecured =
            request -> OPEN_API_ENDPOINTS
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");

        String body = "{\"error\":\"" + err + "\"}";
        org.springframework.core.io.buffer.DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());

        return response.writeWith(Mono.just(buffer));
    }
}