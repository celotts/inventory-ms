package com.celotts.productservice.domain.model;

import java.io.Serializable;

public class CategoryStats implements Serializable {
    private long totalCategories;
    private long activeCategories;
    private long inactiveCategories;

    public CategoryStats() {}

    public CategoryStats(long totalCategories, long activeCategories, long inactiveCategories) {
        this.totalCategories = totalCategories;
        this.activeCategories = activeCategories;
        this.inactiveCategories = inactiveCategories;
    }

    // Builder “manual” para no depender de Lombok
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long totalCategories;
        private long activeCategories;
        private long inactiveCategories;
        public Builder totalCategories(long v){ this.totalCategories = v; return this; }
        public Builder activeCategories(long v){ this.activeCategories = v; return this; }
        public Builder inactiveCategories(long v){ this.inactiveCategories = v; return this; }
        public CategoryStats build(){ return new CategoryStats(totalCategories, activeCategories, inactiveCategories); }
    }

    public long getTotalCategories() { return totalCategories; }
    public long getActiveCategories() { return activeCategories; }
    public long getInactiveCategories() { return inactiveCategories; }
    public void setTotalCategories(long v) { this.totalCategories = v; }
    public void setActiveCategories(long v) { this.activeCategories = v; }
    public void setInactiveCategories(long v) { this.inactiveCategories = v; }
}