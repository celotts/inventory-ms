package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*; // ✅ Importación que faltaba

class ProductEntityTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity entity = ProductEntity.builder()
                .id(id)
                .code("P001")
                .name("Taco")
                .description("Delicious taco")
                .categoryId(categoryId)
                .unitPrice(BigDecimal.valueOf(12.5))
                .currentStock(100)
                .minimumStock(10)
                .enabled(true)
                .unitCode("UNIT01")
                .brandId(brandId)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("tester")
                .updatedBy("tester2")
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getCode()).isEqualTo("P001");
        assertThat(entity.getName()).isEqualTo("Taco");
        assertThat(entity.getDescription()).isEqualTo("Delicious taco");
        assertThat(entity.getCategoryId()).isEqualTo(categoryId);
        assertThat(entity.getUnitPrice()).isEqualTo(BigDecimal.valueOf(12.5));
        assertThat(entity.getCurrentStock()).isEqualTo(100);
        assertThat(entity.getMinimumStock()).isEqualTo(10);
        assertThat(entity.getEnabled()).isTrue();
        assertThat(entity.getUnitCode()).isEqualTo("UNIT01");
        assertThat(entity.getBrandId()).isEqualTo(brandId);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
        assertThat(entity.getCreatedBy()).isEqualTo("tester");
        assertThat(entity.getUpdatedBy()).isEqualTo("tester2");
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProductEntity entity = new ProductEntity();
        UUID id = UUID.randomUUID();

        entity.setId(id);
        entity.setCode("P002");
        entity.setName("Burrito");
        entity.setDescription("Big burrito");
        entity.setCategoryId(UUID.randomUUID());
        entity.setUnitPrice(BigDecimal.TEN);
        entity.setCurrentStock(50);
        entity.setMinimumStock(5);
        entity.setEnabled(false);
        entity.setUnitCode("UNIT02");
        entity.setBrandId(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setCreatedBy("admin");
        entity.setUpdatedBy("admin2");

        assertThat(entity.getName()).isEqualTo("Burrito");
        assertThat(entity.getCode()).isEqualTo("P002");
    }

    @Test
    void testEqualsHashCodeAndToString() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity product1 = ProductEntity.builder()
                .id(id)
                .code("P123")
                .name("Product A")
                .description("Description A")
                .categoryId(categoryId)
                .unitPrice(new BigDecimal("99.99"))
                .currentStock(10)
                .minimumStock(2)
                .enabled(true)
                .unitCode("UN123")
                .brandId(brandId)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductEntity product2 = ProductEntity.builder()
                .id(id)
                .code("P123")
                .name("Product A")
                .description("Description A")
                .categoryId(categoryId)
                .unitPrice(new BigDecimal("99.99"))
                .currentStock(10)
                .minimumStock(2)
                .enabled(true)
                .unitCode("UN123")
                .brandId(brandId)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());

        String toString = product1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Product A"));
        assertTrue(toString.contains("P123"));
    }

    @Test
    void testEqualsAndHashCodeWithSameReference() {
        ProductEntity entity = ProductEntity.builder().id(UUID.randomUUID()).build();
        // Mismo objeto, debe ser igual
        assertThat(entity.equals(entity)).isTrue();
        assertThat(entity.hashCode()).isEqualTo(entity.hashCode());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        ProductEntity entity = ProductEntity.builder().id(UUID.randomUUID()).build();

        // Comparación con null
        assertThat(entity.equals(null)).isFalse();

        // Comparación con objeto de otra clase
        assertThat(entity.equals("some string")).isFalse();
    }

    @Test
    void testEqualsWithDifferentValues() {
        ProductEntity product1 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .code("P100")
                .name("Product A")
                .build();

        ProductEntity product2 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .code("P200")
                .name("Product B")
                .build();

        assertThat(product1.equals(product2)).isFalse();
        assertThat(product1.hashCode()).isNotEqualTo(product2.hashCode());
    }

    @Test
    void testHashCodeWithNullFields() {
        ProductEntity entity = new ProductEntity();
        // No seteamos nada para que todos los campos sean null
        int hashCode = entity.hashCode();
        assertThat(hashCode).isNotZero(); // Solo asegurar que se genera un hash válido
    }

    @Test
    void testEqualsDifferentObjectWithNullFields() {
        ProductEntity product1 = new ProductEntity();
        ProductEntity product2 = new ProductEntity();

        // Ambos objetos con todos los campos null → deben ser iguales
        assertThat(product1).isEqualTo(product2);
        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }

    @Test
    void testEqualsDifferentObjectOneNullField() {
        UUID id = UUID.randomUUID();
        ProductEntity product1 = ProductEntity.builder().id(id).build();
        ProductEntity product2 = new ProductEntity(); // id = null

        // Diferencia en campo `id` → deben ser distintos
        assertThat(product1).isNotEqualTo(product2);
        assertThat(product1.hashCode()).isNotEqualTo(product2.hashCode());
    }

    @Test
    void testEqualsFieldByFieldDifference() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity product1 = ProductEntity.builder()
                .id(id)
                .code("P001")
                .name("Product A")
                .description("Test")
                .categoryId(categoryId)
                .unitPrice(BigDecimal.ONE)
                .currentStock(10)
                .minimumStock(2)
                .enabled(true)
                .unitCode("U1")
                .brandId(brandId)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        // Clon pero con un campo diferente (unitCode)
        ProductEntity product2 = product1.toBuilder()
                .unitCode("DIFFERENT")
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEqualsFieldNullVsNotNull() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity product1 = ProductEntity.builder()
                .id(id)
                .code(null) // null
                .name("Product A")
                .description("Desc A")
                .categoryId(UUID.randomUUID())
                .unitPrice(BigDecimal.ONE)
                .currentStock(10)
                .minimumStock(1)
                .enabled(true)
                .unitCode("U1")
                .brandId(UUID.randomUUID())
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductEntity product2 = product1.toBuilder()
                .code("P999") // not null → diferencia aquí
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEqualsWithNullFieldsCombination() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity product1 = ProductEntity.builder()
                .id(id)
                .code("P001")
                .name("Test Product")
                .description(null) // null
                .categoryId(UUID.randomUUID())
                .unitPrice(BigDecimal.ONE)
                .currentStock(10)
                .minimumStock(5)
                .enabled(true)
                .unitCode("UNIT")
                .brandId(UUID.randomUUID())
                .createdAt(now)
                .updatedAt(null) // null
                .createdBy("admin")
                .updatedBy(null) // null
                .build();

        ProductEntity product2 = product1.toBuilder()
                .description("Some description") // diferente
                .updatedAt(now)                 // diferente
                .updatedBy("someone")           // diferente
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEquals_descriptionNullVsNonNull() {
        ProductEntity product1 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Product")
                .description(null)
                .build();

        ProductEntity product2 = product1.toBuilder()
                .description("Non-null description")
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEqualsWithOnlyOneDifferentField() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity baseProduct = ProductEntity.builder()
                .id(id)
                .code("P001")
                .name("Product A")
                .description("Description A")
                .categoryId(categoryId)
                .unitPrice(BigDecimal.ONE)
                .currentStock(10)
                .minimumStock(1)
                .enabled(true)
                .unitCode("U1")
                .brandId(brandId)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        // Cambiar cada campo uno por uno (ejemplo con `name`)
        ProductEntity differentName = baseProduct.toBuilder()
                .name("Different Name")
                .build();

        assertThat(baseProduct).isNotEqualTo(differentName);
    }

    @Test
    void testEqualsDifferentBooleanField() {
        UUID id = UUID.randomUUID();

        ProductEntity product1 = ProductEntity.builder()
                .id(id)
                .code("P001")
                .name("Same")
                .enabled(true)
                .build();

        ProductEntity product2 = product1.toBuilder()
                .enabled(false)
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEqualsDifferentIntegerField() {
        ProductEntity product1 = ProductEntity.builder()
                .currentStock(100)
                .build();

        ProductEntity product2 = product1.toBuilder()
                .currentStock(200)
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEqualsDifferentBigDecimalScale() {
        ProductEntity product1 = ProductEntity.builder()
                .unitPrice(new BigDecimal("10.0"))
                .build();

        ProductEntity product2 = product1.toBuilder()
                .unitPrice(new BigDecimal("10.00")) // diferente escala
                .build();

        assertThat(product1).isNotEqualTo(product2); // porque equals() de BigDecimal compara escala también
    }

    @Test
    void testEqualsDifferentUUIDField() {
        ProductEntity product1 = ProductEntity.builder()
                .id(UUID.randomUUID())
                .brandId(UUID.randomUUID())
                .build();

        ProductEntity product2 = product1.toBuilder()
                .brandId(UUID.randomUUID())
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEquals_createdByNullInOneOnly_shouldReturnFalse() {
        ProductEntity product1 = ProductEntity.builder()
                .code("P001")
                .name("Product A")
                .categoryId(UUID.randomUUID())
                .unitPrice(new BigDecimal("10.00"))
                .currentStock(10)
                .minimumStock(2)
                .enabled(true)
                .unitCode("KG")
                .brandId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .createdBy(null)
                .build();

        ProductEntity product2 = product1.toBuilder()
                .createdBy("admin")
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void testEquals_differentUnitCode_shouldReturnFalse() {
        ProductEntity product1 = ProductEntity.builder()
                .code("P001")
                .name("Product A")
                .categoryId(UUID.randomUUID())
                .unitPrice(new BigDecimal("10.00"))
                .currentStock(10)
                .minimumStock(2)
                .enabled(true)
                .unitCode("KG")
                .brandId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        ProductEntity product2 = product1.toBuilder()
                .unitCode("LTS")
                .build();

        assertThat(product1).isNotEqualTo(product2);
    }
}