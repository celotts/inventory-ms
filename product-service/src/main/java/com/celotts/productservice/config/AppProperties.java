package com.celotts.productservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter @Setter
public class AppProperties {
    private Pagination pagination;
    @Getter @Setter
    public static class Pagination {
        private Integer defaultPage;
        private Integer defaultSize;
        private Integer maxSize;
        private String  defaultSort;
        private String  defaultDirection;
    }
}