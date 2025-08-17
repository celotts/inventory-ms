package com.celotts.productservice.domain.model.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeModelTest {

    @Test
    void builder_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel m = ProductTypeModel.builder()
                .id(id)
                .code("TYPE-A")
                .name("Type A")
                .description("Primary product type")
                .enabled(true)
                .createdAt(now.minusDays(2))
                .updatedAt(now)
                .createdBy("creator")
                .updatedBy("updater")
                .build();

        assertEquals(id, m.getId());
        assertEquals("TYPE-A", m.getCode());
        assertEquals("Type A", m.getName());
        assertEquals("Primary product type", m.getDescription());
        assertTrue(m.getEnabled());
        assertEquals(now.minusDays(2), m.getCreatedAt());
        assertEquals(now, m.getUpdatedAt());
        assertEquals("creator", m.getCreatedBy());
        assertEquals("updater", m.getUpdatedBy());
    }

    @Test
    void setters_shouldAssignValues() {
        ProductTypeModel m = new ProductTypeModel();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        m.setId(id);
        m.setCode("TYPE-B");
        m.setName("Type B");
        m.setDescription("Secondary");
        m.setEnabled(false);
        m.setCreatedAt(now.minusHours(5));
        m.setUpdatedAt(now);
        m.setCreatedBy("system");
        m.setUpdatedBy("tester");

        assertEquals(id, m.getId());
        assertEquals("TYPE-B", m.getCode());
        assertEquals("Type B", m.getName());
        assertEquals("Secondary", m.getDescription());
        assertFalse(m.getEnabled());
        assertEquals(now.minusHours(5), m.getCreatedAt());
        assertEquals(now, m.getUpdatedAt());
        assertEquals("system", m.getCreatedBy());
        assertEquals("tester", m.getUpdatedBy());
    }

    @Test
    void allArgsConstructor_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(3);
        LocalDateTime updated = LocalDateTime.now();

        ProductTypeModel m = new ProductTypeModel(
                id, "TYPE-C", "Type C", "Desc C", true,
                created, updated, "cby", "uby"
        );

        assertEquals(id, m.getId());
        assertEquals("TYPE-C", m.getCode());
        assertEquals("Type C", m.getName());
        assertEquals("Desc C", m.getDescription());
        assertTrue(m.getEnabled());
        assertEquals(created, m.getCreatedAt());
        assertEquals(updated, m.getUpdatedAt());
        assertEquals("cby", m.getCreatedBy());
        assertEquals("uby", m.getUpdatedBy());
    }

    @Test
    void withers_shouldCreateModifiedCopies_withoutMutatingOriginal() {
        ProductTypeModel base = ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .code("TYPE-D")
                .name("Type D")
                .enabled(false)
                .createdBy("a")
                .updatedBy("b")
                .build();

        ProductTypeModel modified = base
                .withEnabled(true)
                .withName("Type D+")
                .withUpdatedBy("c");

        // Original no cambia
        assertEquals("Type D", base.getName());
        assertFalse(base.getEnabled());
        assertEquals("b", base.getUpdatedBy());

        // Copia refleja cambios
        assertEquals("Type D+", modified.getName());
        assertTrue(modified.getEnabled());
        assertEquals("c", modified.getUpdatedBy());

        // Campos no modificados permanecen iguales
        assertEquals(base.getId(), modified.getId());
        assertEquals(base.getCode(), modified.getCode());
        assertEquals(base.getCreatedBy(), modified.getCreatedBy());
    }

    @Test
    void toBuilder_shouldCloneAndModify() {
        ProductTypeModel original = ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .code("TYPE-E")
                .name("Type E")
                .enabled(true)
                .createdBy("x")
                .updatedBy("y")
                .build();

        ProductTypeModel clone = original.toBuilder()
                .code("TYPE-E2")
                .enabled(false)
                .build();

        assertEquals(original.getId(), clone.getId());
        assertEquals("TYPE-E2", clone.getCode());
        assertEquals(original.getName(), clone.getName());
        assertFalse(clone.getEnabled());
        assertEquals("x", clone.getCreatedBy());
        assertEquals("y", clone.getUpdatedBy());
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        UUID id = UUID.randomUUID();

        ProductTypeModel a = ProductTypeModel.builder()
                .id(id).code("TYPE-X").name("Type X").enabled(true).build();

        ProductTypeModel b = ProductTypeModel.builder()
                .id(id).code("TYPE-X").name("Type X").enabled(true).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndKeyFields() {
        ProductTypeModel m = ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .code("TYPE-Z")
                .name("Type Z")
                .createdBy("tester")
                .enabled(true)
                .build();

        String s = m.toString();
        assertTrue(s.contains("ProductTypeModel"));
        assertTrue(s.contains("TYPE-Z"));
        assertTrue(s.contains("Type Z"));
        assertTrue(s.contains("tester"));
    }
}