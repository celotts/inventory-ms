package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductPriceHistoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductPriceHistoryEntityTest {

    @Test
    @DisplayName("Builder y constructor all-args funcionan correctamente")
    void builderAndAllArgsConstructorWork() {
        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        BigDecimal price = new BigDecimal("123.45");
        LocalDateTime createdAt = LocalDateTime.now();

        ProductPriceHistoryEntity entity = new ProductPriceHistoryEntity(
                id, product, price, createdAt
        );

        assertEquals(id, entity.getId());
        assertEquals(product, entity.getProduct());
        assertEquals(price, entity.getPrice());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    @DisplayName("Getters y setters funcionan correctamente")
    void gettersAndSettersWork() {
        ProductPriceHistoryEntity entity = new ProductPriceHistoryEntity();
        UUID id = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        BigDecimal price = new BigDecimal("999.99");
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setProduct(product);
        entity.setPrice(price);
        entity.setCreatedAt(now);

        assertAll(
                () -> assertEquals(id, entity.getId()),
                () -> assertEquals(product, entity.getProduct()),
                () -> assertEquals(price, entity.getPrice()),
                () -> assertEquals(now, entity.getCreatedAt())
        );
    }

    @Test
    @DisplayName("equals y hashCode funcionan (Lombok @Data)")
    void equalsAndHashCode() {
        UUID id = UUID.randomUUID();
        ProductEntity p = new ProductEntity();
        BigDecimal price = new BigDecimal("10.00");
        LocalDateTime t = LocalDateTime.now();

        ProductPriceHistoryEntity a = new ProductPriceHistoryEntity(id, p, price, t);
        ProductPriceHistoryEntity b = new ProductPriceHistoryEntity(id, p, price, t);
        ProductPriceHistoryEntity c = new ProductPriceHistoryEntity(UUID.randomUUID(), p, price, t);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    @DisplayName("toString incluye información relevante")
    void toStringIncludesFields() {
        ProductPriceHistoryEntity e = new ProductPriceHistoryEntity();
        e.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        e.setPrice(new BigDecimal("1.00"));
        String s = e.toString();
        assertTrue(s.contains("ProductPriceHistoryEntity"));
        assertTrue(s.contains("1.00"));
    }

    @Test
    @DisplayName("Anotaciones JPA están presentes y correctas")
    void jpaAnnotationsArePresent() throws Exception {
        assertNotNull(ProductPriceHistoryEntity.class.getAnnotation(Entity.class));
        Table tableAnn = ProductPriceHistoryEntity.class.getAnnotation(Table.class);
        assertNotNull(tableAnn);
        assertEquals("product_price_history", tableAnn.name());

        Field id = ProductPriceHistoryEntity.class.getDeclaredField("id");
        assertNotNull(id.getAnnotation(Id.class));
        assertNotNull(id.getAnnotation(GeneratedValue.class));

        Field product = ProductPriceHistoryEntity.class.getDeclaredField("product");
        assertNotNull(product.getAnnotation(ManyToOne.class));
        JoinColumn joinColumn = product.getAnnotation(JoinColumn.class);
        assertNotNull(joinColumn);
        assertEquals("product_id", joinColumn.name());
        assertFalse(joinColumn.nullable());

        Field price = ProductPriceHistoryEntity.class.getDeclaredField("price");
        Column priceCol = price.getAnnotation(Column.class);
        assertNotNull(priceCol);
        assertFalse(priceCol.nullable());
        assertEquals(10, priceCol.precision());
        assertEquals(2, priceCol.scale());

        Field createdAt = ProductPriceHistoryEntity.class.getDeclaredField("createdAt");
        Column createdAtCol = createdAt.getAnnotation(Column.class);
        assertNotNull(createdAtCol);
        assertEquals("created_at", createdAtCol.name());
    }
}
