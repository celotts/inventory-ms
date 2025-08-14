package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductImageEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductImageEntityTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String code = "PRD-IMG-01";

        ProductEntity product = ProductEntity.builder()
                .id(productId)
                .code(code)
                .name("Producto imagen")
                .enabled(true)
                .build();

        String url = "https://cdn.example.com/img01.jpg";
        LocalDateTime uploadedAt = LocalDateTime.now().minusDays(1);

        ProductImageEntity image = new ProductImageEntity(id, product, url, uploadedAt);

        assertEquals(id, image.getId());
        assertEquals(product, image.getProduct());
        assertEquals(url, image.getUrl());
        assertEquals(uploadedAt, image.getUploadedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProductImageEntity image = new ProductImageEntity();

        UUID id = UUID.randomUUID();
        ProductEntity product = ProductEntity.builder()
                .id(UUID.randomUUID())
                .code("PRD-001")
                .name("Producto")
                .build();

        String url = "https://cdn.example.com/photo.jpg";
        LocalDateTime uploadedAt = LocalDateTime.now();

        image.setId(id);
        image.setProduct(product);
        image.setUrl(url);
        image.setUploadedAt(uploadedAt);

        assertEquals(id, image.getId());
        assertEquals(product, image.getProduct());
        assertEquals(url, image.getUrl());
        assertEquals(uploadedAt, image.getUploadedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        ProductEntity product = ProductEntity.builder()
                .id(UUID.randomUUID())
                .code("PRD-X")
                .build();

        String url = "https://cdn.example.com/x.jpg";
        LocalDateTime date = LocalDateTime.now().minusDays(2);

        ProductImageEntity i1 = new ProductImageEntity(id, product, url, date);
        ProductImageEntity i2 = new ProductImageEntity(id, product, url, date);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentObjects() {
        ProductImageEntity image = new ProductImageEntity();
        assertNotEquals(image, null);
        assertNotEquals(image, "not-an-image");
    }

    @Test
    void testToString() {
        ProductImageEntity image = new ProductImageEntity();
        image.setUrl("https://example.com/image.png");

        String str = image.toString();

        assertNotNull(str);
        assertTrue(str.contains("https://example.com/image.png"));
    }
}