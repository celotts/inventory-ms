package com.celotts.productservice.domain.model.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitModelTest {

    @Test
    void builder_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitModel m = ProductUnitModel.builder()
                .id(id)
                .code("U-001")
                .name("UnitTest")
                .description("Test unit")
                .symbol("UT")
                .enabled(true)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .createdBy("creator")
                .updatedBy("updater")
                .build();

        assertEquals(id, m.getId());
        assertEquals("U-001", m.getCode());
        assertEquals("UnitTest", m.getName());
        assertEquals("Test unit", m.getDescription());
        assertEquals("UT", m.getSymbol());
        assertTrue(m.getEnabled());
        assertEquals(now.minusDays(1), m.getCreatedAt());
        assertEquals(now, m.getUpdatedAt());
        assertEquals("creator", m.getCreatedBy());
        assertEquals("updater", m.getUpdatedBy());
    }

    @Test
    void setters_shouldAssignValues() {
        ProductUnitModel m = new ProductUnitModel();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        m.setId(id);
        m.setCode("U-002");
        m.setName("SetterUnit");
        m.setDescription("Setters test");
        m.setSymbol("SU");
        m.setEnabled(false);
        m.setCreatedAt(now.minusDays(2));
        m.setUpdatedAt(now);
        m.setCreatedBy("system");
        m.setUpdatedBy("tester");

        assertEquals(id, m.getId());
        assertEquals("U-002", m.getCode());
        assertEquals("SetterUnit", m.getName());
        assertEquals("Setters test", m.getDescription());
        assertEquals("SU", m.getSymbol());
        assertFalse(m.getEnabled());
        assertEquals(now.minusDays(2), m.getCreatedAt());
        assertEquals(now, m.getUpdatedAt());
        assertEquals("system", m.getCreatedBy());
        assertEquals("tester", m.getUpdatedBy());
    }

    @Test
    void allArgsConstructor_shouldWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(5);
        LocalDateTime updated = LocalDateTime.now();

        ProductUnitModel m = new ProductUnitModel(
                id, "U-003", "AllArgs", "All args ctor", "AA",
                true, created, updated, "cby", "uby"
        );

        assertEquals(id, m.getId());
        assertEquals("U-003", m.getCode());
        assertEquals("AllArgs", m.getName());
        assertEquals("All args ctor", m.getDescription());
        assertEquals("AA", m.getSymbol());
        assertTrue(m.getEnabled());
        assertEquals(created, m.getCreatedAt());
        assertEquals(updated, m.getUpdatedAt());
        assertEquals("cby", m.getCreatedBy());
        assertEquals("uby", m.getUpdatedBy());
    }

    @Test
    void withers_shouldCreateModifiedCopies_withoutMutatingOriginal() {
        ProductUnitModel base = ProductUnitModel.builder()
                .id(UUID.randomUUID())
                .code("U-004")
                .name("BaseUnit")
                .symbol("BU")
                .enabled(false)
                .createdBy("a")
                .updatedBy("b")
                .build();

        ProductUnitModel modified = base
                .withEnabled(true)
                .withName("ModifiedUnit")
                .withUpdatedBy("c");

        // original intacto
        assertEquals("BaseUnit", base.getName());
        assertFalse(base.getEnabled());
        assertEquals("b", base.getUpdatedBy());

        // copia modificada
        assertEquals("ModifiedUnit", modified.getName());
        assertTrue(modified.getEnabled());
        assertEquals("c", modified.getUpdatedBy());

        // campos no tocados se conservan
        assertEquals(base.getId(), modified.getId());
        assertEquals(base.getCode(), modified.getCode());
        assertEquals(base.getSymbol(), modified.getSymbol());
        assertEquals(base.getCreatedBy(), modified.getCreatedBy());
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        UUID id = UUID.randomUUID();

        ProductUnitModel a = ProductUnitModel.builder()
                .id(id).code("U-006").name("EqualUnit").enabled(true).build();

        ProductUnitModel b = ProductUnitModel.builder()
                .id(id).code("U-006").name("EqualUnit").enabled(true).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndFields() {
        ProductUnitModel m = ProductUnitModel.builder()
                .id(UUID.randomUUID())
                .code("U-007")
                .name("PrettyUnit")
                .createdBy("tester")
                .enabled(true)
                .build();

        String s = m.toString();
        assertTrue(s.contains("ProductUnitModel"));
        assertTrue(s.contains("U-007"));
        assertTrue(s.contains("PrettyUnit"));
        assertTrue(s.contains("tester"));
    }
}