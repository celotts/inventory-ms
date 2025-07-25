package com.celotts.productservice.infrastructure.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.pagination")
@Getter
public class PaginationProperties {
    private int defaultPage;
    private int defaultSize;
    private int maxSize;
    private String defaultSort;
    private String defaultDirection;
}