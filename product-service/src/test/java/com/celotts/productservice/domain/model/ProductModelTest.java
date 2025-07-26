package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    @Test
    void lowStock_shouldReturnFalse_whenCurrentStockIsNull() {
        ProductModel product = ProductModel.builder()
                .minimumStock(10)
                .currentStock(null)
                .build();

        assertFalse(product.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalse_whenMinimumStockIsNull() {
        ProductModel product = ProductModel.builder()
                .currentStock(5)
                .minimumStock(null)
                .build();

        assertFalse(product.lowStock());
    }

    @Test
    void lowStock_shouldReturnTrue_whenCurrentStockLessThanMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(5)
                .minimumStock(10)
                .build();

        assertTrue(product.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalse_whenCurrentStockGreaterOrEqualToMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(15)
                .minimumStock(10)
                .build();

        assertFalse(product.lowStock());
    }
}