package com.celotts.configservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    // IMPORTANTE: Inicializar con "new" para que nunca sea null
    private Cors cors = new Cors();
    private Pagination pagination = new Pagination();

    public Cors getCors() { return cors; }
    public void setCors(Cors cors) { this.cors = cors; }

    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination pagination) { this.pagination = pagination; }

    public static class Cors {
        private String allowedOrigin = "*";

        public String getAllowedOrigin() { return allowedOrigin; }
        public void setAllowedOrigin(String allowedOrigin) { this.allowedOrigin = allowedOrigin; }
    }

    public static class Pagination {
        private int defaultPage = 0;
        private int defaultSize = 10;
        private int maxSize = 100;
        private String defaultSort = "createdAt";
        private String defaultDirection = "desc";

        // Getters
        public int getDefaultPage() { return defaultPage; }
        public int getDefaultSize() { return defaultSize; }
        public int getMaxSize() { return maxSize; }
        public String getDefaultSort() { return defaultSort; }
        public String getDefaultDirection() { return defaultDirection; }

        // Setters
        public void setDefaultPage(int defaultPage) { this.defaultPage = defaultPage; }
        public void setDefaultSize(int defaultSize) { this.defaultSize = defaultSize; }
        public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
        public void setDefaultSort(String defaultSort) { this.defaultSort = defaultSort; }
        public void setDefaultDirection(String defaultDirection) { this.defaultDirection = defaultDirection; }
    }
}
