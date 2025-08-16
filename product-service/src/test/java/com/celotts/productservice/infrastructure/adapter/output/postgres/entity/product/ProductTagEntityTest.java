package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTagEntityTest {

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2024, 1, 1, 10, 0, 0);

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Tag";
        String description = "A test tag";

        ProductTagEntity tag = new ProductTagEntity(id, name, description, FIXED_TIME);

        assertEquals(id, tag.getId());
        assertEquals(name, tag.getName());
        assertEquals(description, tag.getDescription());
        assertEquals(FIXED_TIME, tag.getCreatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProductTagEntity tag = new ProductTagEntity();

        UUID id = UUID.randomUUID();
        tag.setId(id);
        tag.setName("Set Name");
        tag.setDescription("Set Description");
        tag.setCreatedAt(FIXED_TIME);

        assertEquals(id, tag.getId());
        assertEquals("Set Name", tag.getName());
        assertEquals("Set Description", tag.getDescription());
        assertEquals(FIXED_TIME, tag.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();

        ProductTagEntity tag1 = new ProductTagEntity(id, "TAG", "desc", FIXED_TIME);
        ProductTagEntity tag2 = new ProductTagEntity(id, "TAG", "desc", FIXED_TIME);

        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        UUID id = UUID.randomUUID();

        ProductTagEntity tag1 = new ProductTagEntity(id, "TAG1", "desc1", FIXED_TIME);
        ProductTagEntity tag2 = new ProductTagEntity(id, "TAG2", "desc2", FIXED_TIME);

        assertNotEquals(tag1, tag2);
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testEquals_nullAndDifferentClass() {
        ProductTagEntity tag = new ProductTagEntity();

        assertNotEquals(tag, null);
        assertNotEquals(tag, new Object());
    }

    @Test
    void testEquals_sameInstance() {
        ProductTagEntity tag = new ProductTagEntity();
        assertEquals(tag, tag);
    }

    @Test
    void testEquals_nameIsNullInOneObject() {
        UUID id = UUID.randomUUID();

        ProductTagEntity tag1 = new ProductTagEntity(id, null, "desc", FIXED_TIME);
        ProductTagEntity tag2 = new ProductTagEntity(id, "TAG", "desc", FIXED_TIME);

        assertNotEquals(tag1, tag2);
    }

    @Test
    void testEquals_descriptionDifferent() {
        UUID id = UUID.randomUUID();
        String name = "TAG";

        ProductTagEntity tag1 = new ProductTagEntity(id, name, "desc1", FIXED_TIME);
        ProductTagEntity tag2 = new ProductTagEntity(id, name, "desc2", FIXED_TIME);

        assertNotEquals(tag1, tag2);
    }

    @Test
    void testEquals_createdAtDifferent() {
        UUID id = UUID.randomUUID();
        String name = "TAG";
        String desc = "desc";

        ProductTagEntity tag1 = new ProductTagEntity(id, name, desc, FIXED_TIME);
        ProductTagEntity tag2 = new ProductTagEntity(id, name, desc, FIXED_TIME.plusDays(1));

        assertNotEquals(tag1, tag2);
    }

    @Test
    void testToString_shouldReturnStringRepresentation() {
        UUID id = UUID.randomUUID();
        String name = "TestTag";
        String description = "Some description";

        ProductTagEntity tag = new ProductTagEntity(id, name, description, FIXED_TIME);

        String result = tag.toString();

        assertNotNull(result);
        assertTrue(result.contains("TestTag"));
        assertTrue(result.contains("Some description"));
        assertTrue(result.contains("ProductTagEntity"));
    }

    @Test
    void testCanEqual() {
        ProductTagEntity tag1 = new ProductTagEntity();
        ProductTagEntity tag2 = new ProductTagEntity();

        assertTrue(tag1.canEqual(tag2));
        assertFalse(tag1.canEqual(new Object()));
    }

    @Test
    void builder_covers_setters_build_and_builderToString() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Usamos el builder y llamamos a toString() del BUILDER antes de build()
        ProductTagEntity.ProductTagEntityBuilder builder = ProductTagEntity.builder()
                .id(id)
                .name("summer")
                .description("seasonal tag")
                .createdAt(now);

        String builderStr = builder.toString(); // cubre toString() del builder
        assertNotNull(builderStr);
        assertTrue(builderStr.contains("name=summer"));

        ProductTagEntity tag = builder.build(); // cubre build()

        // Verificamos la instancia resultante
        assertEquals(id, tag.getId());
        assertEquals("summer", tag.getName());
        assertEquals("seasonal tag", tag.getDescription());
        assertEquals(now, tag.getCreatedAt());

        // toString() de la ENTIDAD (generado por Lombok @Data)
        assertNotNull(tag.toString());

        // equals/hashCode básicos (también generados por @Data)
        ProductTagEntity tag2 = ProductTagEntity.builder()
                .id(id)
                .name("summer")
                .description("seasonal tag")
                .createdAt(now)
                .build();

        assertEquals(tag, tag2);
        assertEquals(tag.hashCode(), tag2.hashCode());
    }
}