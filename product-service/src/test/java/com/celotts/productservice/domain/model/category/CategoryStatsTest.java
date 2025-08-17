package com.celotts.productservice.domain.model.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStatsTest {

    @Test
    void noArgsConstructor_and_setters_shouldWork() {
        CategoryStats stats = new CategoryStats();
        stats.setTotalCategories(10);
        stats.setActiveCategories(6);
        stats.setInactiveCategories(4);

        assertEquals(10, stats.getTotalCategories());
        assertEquals(6, stats.getActiveCategories());
        assertEquals(4, stats.getInactiveCategories());
    }

    @Test
    void allArgsConstructor_shouldAssignFields() {
        CategoryStats stats = new CategoryStats(20, 12, 8);

        assertEquals(20, stats.getTotalCategories());
        assertEquals(12, stats.getActiveCategories());
        assertEquals(8, stats.getInactiveCategories());
    }

    @Test
    void builder_shouldCreateInstanceCorrectly() {
        CategoryStats stats = CategoryStats.builder()
                .totalCategories(50)
                .activeCategories(30)
                .inactiveCategories(20)
                .build();

        assertEquals(50, stats.getTotalCategories());
        assertEquals(30, stats.getActiveCategories());
        assertEquals(20, stats.getInactiveCategories());
    }

    @Test
    void setters_shouldOverrideValues() {
        CategoryStats stats = new CategoryStats(5, 3, 2);

        stats.setTotalCategories(15);
        stats.setActiveCategories(10);
        stats.setInactiveCategories(5);

        assertEquals(15, stats.getTotalCategories());
        assertEquals(10, stats.getActiveCategories());
        assertEquals(5, stats.getInactiveCategories());
    }
}