package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTagEntityTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Tag";
        String description = "A test tag";
        LocalDateTime createdAt = LocalDateTime.now();

        ProductTagEntity tag = new ProductTagEntity(id, name, description, createdAt);

        assertEquals(id, tag.getId());
        assertEquals(name, tag.getName());
        assertEquals(description, tag.getDescription());
        assertEquals(createdAt, tag.getCreatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ProductTagEntity tag = new ProductTagEntity();

        UUID id = UUID.randomUUID();
        tag.setId(id);
        tag.setName("Set Name");
        tag.setDescription("Set Description");
        LocalDateTime now = LocalDateTime.now();
        tag.setCreatedAt(now);

        assertEquals(id, tag.getId());
        assertEquals("Set Name", tag.getName());
        assertEquals("Set Description", tag.getDescription());
        assertEquals(now, tag.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        ProductTagEntity tag1 = new ProductTagEntity(id, "TAG", "desc", createdAt);
        ProductTagEntity tag2 = new ProductTagEntity(id, "TAG", "desc", createdAt);

        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        UUID id = UUID.randomUUID();

        ProductTagEntity tag1 = new ProductTagEntity(id, "TAG1", "desc1", LocalDateTime.now());
        ProductTagEntity tag2 = new ProductTagEntity(id, "TAG2", "desc2", LocalDateTime.now());

        assertNotEquals(tag1, tag2);
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testEquals_nullAndDifferentClass() {
        ProductTagEntity tag = new ProductTagEntity();

        assertNotEquals(tag, null);
        assertNotEquals(tag, "not a ProductTagEntity");
    }

    @Test
    void testEquals_sameInstance() {
        ProductTagEntity tag = new ProductTagEntity();
        assertEquals(tag, tag);
    }

    @Test
    void testEquals_nameIsNullInOneObject() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        ProductTagEntity tag1 = new ProductTagEntity(id, null, "desc", createdAt);
        ProductTagEntity tag2 = new ProductTagEntity(id, "TAG", "desc", createdAt);

        assertNotEquals(tag1, tag2);
    }

    @Test
    void testEquals_descriptionDifferent() {
        UUID id = UUID.randomUUID();
        String name = "TAG";
        LocalDateTime createdAt = LocalDateTime.now();

        ProductTagEntity tag1 = new ProductTagEntity(id, name, "desc1", createdAt);
        ProductTagEntity tag2 = new ProductTagEntity(id, name, "desc2", createdAt);

        assertNotEquals(tag1, tag2);
    }

    @Test
    void testEquals_createdAtDifferent() {
        UUID id = UUID.randomUUID();
        String name = "TAG";
        String desc = "desc";

        ProductTagEntity tag1 = new ProductTagEntity(id, name, desc, LocalDateTime.now());
        ProductTagEntity tag2 = new ProductTagEntity(id, name, desc, LocalDateTime.now().plusSeconds(10));

        assertNotEquals(tag1, tag2);
    }

    @Test
    void testToString_shouldReturnStringRepresentation() {
        UUID id = UUID.randomUUID();
        String name = "TestTag";
        String description = "Some description";
        LocalDateTime createdAt = LocalDateTime.now();

        ProductTagEntity tag = new ProductTagEntity(id, name, description, createdAt);

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
        assertFalse(tag1.canEqual("not a ProductTagEntity"));
    }
}