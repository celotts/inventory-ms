package com.celotts.productservice.domain.model;

import com.celotts.productservice.domain.model.ProductModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    @Test
    void builder_shouldBuildProductModelCorrectly() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductModel product = ProductModel.builder()
                .id(id)
                .code("P001")
                .name("Producto Test")
                .description("Descripción del producto")
                .categoryId(categoryId)
                .unitCode("UN")
                .brandId(brandId)
                .minimumStock(10)
                .currentStock(5)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("tester")
                .updatedBy("tester")
                .build();

        assertNotNull(product);
        assertEquals("P001", product.getCode());
        assertEquals("Producto Test", product.getName());
        assertEquals(brandId, product.getBrandId());
        assertEquals(BigDecimal.valueOf(99.99), product.getUnitPrice());
    }



    @Test
    void lowStock_shouldReturnFalseWhenCurrentStockIsGreaterOrEqualToMinimum() {
        ProductModel product1 = ProductModel.builder()
                .currentStock(10)
                .minimumStock(5)
                .build();

        ProductModel product2 = ProductModel.builder()
                .currentStock(5)
                .minimumStock(5)
                .build();

        assertFalse(product1.lowStock());
        assertFalse(product2.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalseWhenCurrentOrMinimumStockIsNull() {
        ProductModel product1 = ProductModel.builder()
                .currentStock(null)
                .minimumStock(5)
                .build();

        ProductModel product2 = ProductModel.builder()
                .currentStock(5)
                .minimumStock(null)
                .build();

        assertFalse(product1.lowStock());
        assertFalse(product2.lowStock());
    }

    @Test
    void withMethod_shouldCreateNewInstanceWithModifiedField() {
        ProductModel original = ProductModel.builder()
                .name("Original")
                .build();

        ProductModel modified = original.withName("Modificado");

        assertEquals("Original", original.getName());
        assertEquals("Modificado", modified.getName());
        assertNotSame(original, modified);
    }

    @Test
    void equalsAndHashCode_shouldBehaveAsExpected() {
        UUID id = UUID.randomUUID();

        ProductModel p1 = ProductModel.builder().id(id).code("X1").build();
        ProductModel p2 = ProductModel.builder().id(id).code("X1").build();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void toString_shouldContainKeyFields() {
        ProductModel product = ProductModel.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("ToStringTest")
                .build();

        String result = product.toString();
        assertTrue(result.contains("ToStringTest"));
        assertTrue(result.contains("00000000-0000-0000-0000-000000000001"));
    }



    @Test
    void noArgsConstructorAndSetters_shouldSetAllFieldsCorrectly_v2() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        BigDecimal unitPrice = new BigDecimal("99.99");

        ProductModel model = new ProductModel();
        model.setId(id);
        model.setCode("PX001");
        model.setName("Nuevo Producto");
        model.setDescription("Descripción larga");
        model.setCategoryId(categoryId);
        model.setUnitCode("UN001");
        model.setBrandId(brandId);
        model.setMinimumStock(10);
        model.setCurrentStock(5);
        model.setUnitPrice(unitPrice);
        model.setEnabled(true);
        model.setCreatedAt(createdAt);
        model.setUpdatedAt(updatedAt);
        model.setCreatedBy("admin");
        model.setUpdatedBy("editor");

        assertEquals(id, model.getId());
        assertEquals("PX001", model.getCode());
        assertEquals("Nuevo Producto", model.getName());
        assertEquals("Descripción larga", model.getDescription());
        assertEquals(categoryId, model.getCategoryId());
        assertEquals("UN001", model.getUnitCode());
        assertEquals(brandId, model.getBrandId());
        assertEquals(10, model.getMinimumStock());
        assertEquals(5, model.getCurrentStock());
        assertEquals(unitPrice, model.getUnitPrice());
        assertTrue(model.getEnabled());
        assertEquals(createdAt, model.getCreatedAt());
        assertEquals(updatedAt, model.getUpdatedAt());
        assertEquals("admin", model.getCreatedBy());
        assertEquals("editor", model.getUpdatedBy());
    }

    @Test
    void withMethods_shouldCreateModifiedCopies() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID newCategoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID newBrandId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime newCreatedAt = createdAt.plusDays(2);
        LocalDateTime updatedAt = createdAt.plusDays(1);
        LocalDateTime newUpdatedAt = updatedAt.plusDays(1);
        BigDecimal price = BigDecimal.valueOf(29.99);
        BigDecimal newPrice = BigDecimal.valueOf(49.99);

        ProductModel original = ProductModel.builder()
                .id(id)
                .code("CODE001")
                .name("Product")
                .description("Desc")
                .categoryId(categoryId)
                .unitCode("UN")
                .brandId(brandId)
                .minimumStock(5)
                .currentStock(10)
                .unitPrice(price)
                .enabled(true)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy("creator")
                .updatedBy("updater")
                .build();

        assertEquals("CODE002", original.withCode("CODE002").getCode());
        assertEquals(newCreatedAt, original.withCreatedAt(newCreatedAt).getCreatedAt());
        assertEquals(newUpdatedAt, original.withUpdatedAt(newUpdatedAt).getUpdatedAt());
        assertEquals("new-creator", original.withCreatedBy("new-creator").getCreatedBy());
        assertEquals("new-updater", original.withUpdatedBy("new-updater").getUpdatedBy());
        assertEquals("new-desc", original.withDescription("new-desc").getDescription());
        assertEquals(newCategoryId, original.withCategoryId(newCategoryId).getCategoryId());
        assertEquals("NEW_UN", original.withUnitCode("NEW_UN").getUnitCode());
        assertEquals(newBrandId, original.withBrandId(newBrandId).getBrandId());
        assertEquals(2, original.withMinimumStock(2).getMinimumStock());
        assertEquals(3, original.withCurrentStock(3).getCurrentStock());
        assertEquals(newPrice, original.withUnitPrice(newPrice).getUnitPrice());
        assertFalse(original.withEnabled(false).getEnabled());
        assertEquals("NewName", original.withName("NewName").getName());
    }

    @Test
    void lowStock_shouldReturnTrueWhenCurrentStockIsLessThanMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(3)
                .minimumStock(5)
                .build();

        assertTrue(product.lowStock()); // current < min
    }

    @Test
    void lowStock_shouldReturnFalseWhenCurrentStockEqualsMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(5)
                .minimumStock(5)
                .build();

        assertFalse(product.lowStock()); // current == min
    }

    @Test
    void lowStock_shouldReturnFalseWhenCurrentStockIsGreaterThanMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(10)
                .minimumStock(5)
                .build();

        assertFalse(product.lowStock()); // current > min
    }

    @Test
    void lowStock_shouldReturnFalseWhenCurrentStockIsNull() {
        ProductModel product = ProductModel.builder()
                .currentStock(null)
                .minimumStock(5)
                .build();

        assertFalse(product.lowStock()); // current == null
    }

    @Test
    void lowStock_shouldReturnFalseWhenMinimumStockIsNull() {
        ProductModel product = ProductModel.builder()
                .currentStock(5)
                .minimumStock(null)
                .build();

        assertFalse(product.lowStock()); // min == null
    }

    @Test
    void lowStock_shouldReturnFalseWhenBothAreNull() {
        ProductModel product = ProductModel.builder()
                .currentStock(null)
                .minimumStock(null)
                .build();

        assertFalse(product.lowStock()); // ambos null
    }

    @Test
    void withId_shouldCreateCopyWithNewId() {
        UUID originalId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();

        ProductModel original = ProductModel.builder()
                .id(originalId)
                .name("Test Product")
                .build();

        ProductModel modified = original.withId(newId);

        assertEquals("Test Product", modified.getName());
        assertEquals(newId, modified.getId());
        assertNotEquals(original.getId(), modified.getId());
    }

    @Test
    void equals_shouldReturnTrue_whenAllFieldsAreNullAndMatch() {
        ProductModel model1 = new ProductModel();
        ProductModel model2 = new ProductModel();

        assertEquals(model1, model2);  // todos null
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentName() {
        UUID id = UUID.randomUUID();
        ProductModel model1 = ProductModel.builder()
                .id(id)
                .name("Product A")
                .build();

        ProductModel model2 = ProductModel.builder()
                .id(id)
                .name("Product B")
                .build();

        assertNotEquals(model1, model2);
    }

    @Test
    void hashCode_shouldBeConsistentWithEquals() {
        UUID id = UUID.randomUUID();
        ProductModel model1 = ProductModel.builder()
                .id(id)
                .name("Same")
                .build();

        ProductModel model2 = ProductModel.builder()
                .id(id)
                .name("Same")
                .build();

        assertEquals(model1, model2);
        assertEquals(model1.hashCode(), model2.hashCode());
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
    void lowStock_shouldReturnFalse_whenCurrentStockGreaterThanMinimum() {
        ProductModel product = ProductModel.builder()
                .currentStock(15)
                .minimumStock(10)
                .build();

        assertFalse(product.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalse_whenStocksAreNull() {
        ProductModel product = new ProductModel();

        assertFalse(product.lowStock());
    }
    @Test
    void equals_shouldReturnTrue_whenSameInstance() {
        ProductModel model = ProductModel.builder().name("Test").build();
        assertEquals(model, model);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToNull() {
        ProductModel model = ProductModel.builder().name("Test").build();
        assertNotEquals(null, model);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentClass() {
        ProductModel model = ProductModel.builder().name("Test").build();
        assertNotEquals("other", model);
    }

    @Test
    void equals_shouldReturnTrue_whenAllFieldsMatch() {
        UUID id = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        String unitCode = "KG";
        String createdBy = "tester";
        String updatedBy = "tester";
        LocalDateTime now = LocalDateTime.now();

        ProductModel model1 = ProductModel.builder()
                .id(id)
                .code("ABC123")
                .name("Product")
                .description("Desc")
                .categoryId(categoryId)
                .unitCode(unitCode)
                .brandId(brandId)
                .minimumStock(10)
                .currentStock(5)
                .unitPrice(BigDecimal.valueOf(100.50))
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();

        ProductModel model2 = model1.toBuilder().build(); // copia exacta
        assertEquals(model1, model2);
    }


    @Test
    void equals_shouldReturnFalse_whenOneFieldDiffers() {
        ProductModel model1 = ProductModel.builder().code("A").build();
        ProductModel model2 = ProductModel.builder().code("B").build();
        assertNotEquals(model1, model2);
    }


    @Test
    void withId_shouldReturnNewInstanceWithModifiedId() {
        ProductModel original = ProductModel.builder().id(UUID.randomUUID()).name("Test").build();
        UUID newId = UUID.randomUUID();
        ProductModel modified = original.withId(newId);

        assertNotEquals(original.getId(), modified.getId());
        assertEquals("Test", modified.getName());
    }

    @Test
    void lowStock_shouldReturnTrue_whenCurrentStockIsLowerThanMinimum() {
        ProductModel model = ProductModel.builder()
                .currentStock(5)
                .minimumStock(10)
                .build();
        assertTrue(model.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalse_whenCurrentStockIsGreaterThanMinimum() {
        ProductModel model = ProductModel.builder()
                .currentStock(20)
                .minimumStock(10)
                .build();
        assertFalse(model.lowStock());
    }

    @Test
    void lowStock_shouldReturnFalse_whenValuesAreNull() {
        ProductModel model = new ProductModel();
        assertFalse(model.lowStock());
    }

}