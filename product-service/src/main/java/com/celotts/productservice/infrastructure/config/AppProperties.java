package com.celotts.productservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Cors cors,
        Pagination pagination
) {
    public record Cors(@DefaultValue("*") String allowedOrigin) {}
    public record Pagination(
            @DefaultValue("0") int defaultPage,
            @DefaultValue("10") int defaultSize,
            @DefaultValue("100") int maxSize,
            @DefaultValue("createdAt") String defaultSort,
            @DefaultValue("desc") String defaultDirection
    ) {}
}