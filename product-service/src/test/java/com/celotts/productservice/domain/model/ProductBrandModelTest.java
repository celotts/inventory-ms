package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandModelTest {

    @Test
    void shouldReturnTrueWhenEnabledIsTrue() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .enabled(true)
                .build();

        assertTrue(brand.isActive());
    }

    @Test
    void shouldReturnFalseWhenEnabledIsNull() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .enabled(null)
                .build();

        assertFalse(brand.isActive());
    }

    @Test
    void shouldReturnFalseWhenEnabledIsFalse() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .enabled(false)
                .build();

        assertFalse(brand.isActive());
    }

    @Test
    void shouldActivateBrand() {
        ProductBrandModel brand = new ProductBrandModel();
        brand.activate();

        assertTrue(brand.getEnabled());
    }

    @Test
    void shouldDeactivateBrand() {
        ProductBrandModel brand = new ProductBrandModel();
        brand.deactivate();

        assertFalse(brand.getEnabled());
    }
}