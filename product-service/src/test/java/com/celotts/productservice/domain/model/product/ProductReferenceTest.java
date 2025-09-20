package com.celotts.productservice.domain.model.product;

import com.celotts.productservice.domain.contract.product.ProductReference;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductReferenceTest {

    static class DummyProductReference implements ProductReference {
        private final String code = "P-001";
        private final String name = "Laptop";
        private final String description = "High performance laptop";
        private final UUID categoryId = UUID.randomUUID();
        private final String unitCode = "PCS";
        private final UUID brandId = UUID.randomUUID();
        private final Integer minimumStock = 5;
        private final Integer currentStock = 12;
        private final BigDecimal unitPrice = new BigDecimal("1499.99");
        private final Boolean enabled = true;
        private final String createdBy = "tester";
        private final String updatedBy = "editor";

        @Override public String getCode() { return code; }
        @Override public String getName() { return name; }
        @Override public String getDescription() { return description; }
        @Override public UUID getCategoryId() { return categoryId; }
        @Override public String getUnitCode() { return unitCode; }
        @Override public UUID getBrandId() { return brandId; }
        @Override public Integer getMinimumStock() { return minimumStock; }
        @Override public Integer getCurrentStock() { return currentStock; }
        @Override public BigDecimal getUnitPrice() { return unitPrice; }
        @Override public Boolean getEnabled() { return enabled; }
        @Override public String getCreatedBy() { return createdBy; }
        @Override public String getUpdatedBy() { return updatedBy; }
    }

    @Test
    void dummyImplementation_shouldReturnExpectedValues() {
        ProductReference ref = new DummyProductReference();

        assertEquals("P-001", ref.getCode());
        assertEquals("Laptop", ref.getName());
        assertEquals("High performance laptop", ref.getDescription());
        assertNotNull(ref.getCategoryId());
        assertEquals("PCS", ref.getUnitCode());
        assertNotNull(ref.getBrandId());
        assertEquals(5, ref.getMinimumStock());
        assertEquals(12, ref.getCurrentStock());
        assertEquals(new BigDecimal("1499.99"), ref.getUnitPrice());
        assertTrue(ref.getEnabled());
        assertEquals("tester", ref.getCreatedBy());
        assertEquals("editor", ref.getUpdatedBy());
    }
}