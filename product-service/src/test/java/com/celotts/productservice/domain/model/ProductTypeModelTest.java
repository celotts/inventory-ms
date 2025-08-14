package com.celotts.productservice.domain.model;

import com.celotts.productserviceOld.domain.model.ProductTypeModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeModelTest {

    private ProductTypeModel base() {
        return ProductTypeModel.builder()
                .id(UUID.randomUUID())
                .code("PT-001")
                .name("Type A")
                .descpiption("Desc A")  // ojo: campo escrito como 'descpiption'
                .enabled(true)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .createdBy("creator")
                .updateBy("updater")     // ojo: 'updateBy' (no 'updatedBy')
                .build();
    }

    @Test
    void builder_shouldBuildAllFields() {
        ProductTypeModel m = base();
        assertNotNull(m.getId());
        assertEquals("PT-001", m.getCode());
        assertEquals("Type A", m.getName());
        assertEquals("Desc A", m.getDescpiption());
        assertTrue(m.getEnabled());
        assertNotNull(m.getCreatedAt());
        assertNotNull(m.getUpdatedAt());
        assertEquals("creator", m.getCreatedBy());
        assertEquals("updater", m.getUpdateBy());
    }

    @Test
    void noArgsConstructor_and_setters_shouldWork() {
        ProductTypeModel m = new ProductTypeModel();

        UUID id = UUID.randomUUID();
        LocalDateTime c = LocalDateTime.now().minusDays(3);
        LocalDateTime u = LocalDateTime.now().minusDays(2);

        m.setId(id);
        m.setCode("X");
        m.setName("Y");
        m.setDescpiption("Z");
        m.setEnabled(false);
        m.setCreatedAt(c);
        m.setUpdatedAt(u);
        m.setCreatedBy("cb");
        m.setUpdateBy("ub");

        assertEquals(id, m.getId());
        assertEquals("X", m.getCode());
        assertEquals("Y", m.getName());
        assertEquals("Z", m.getDescpiption());
        assertFalse(m.getEnabled());
        assertEquals(c, m.getCreatedAt());
        assertEquals(u, m.getUpdatedAt());
        assertEquals("cb", m.getCreatedBy());
        assertEquals("ub", m.getUpdateBy());
    }

    @Test
    void allArgsConstructor_shouldAssignAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime c = LocalDateTime.now().minusDays(4);
        LocalDateTime u = LocalDateTime.now().minusDays(1);

        ProductTypeModel m = new ProductTypeModel(id, "C1", "N1", "D1", true, c, u, "creator", "updater");

        assertEquals(id, m.getId());
        assertEquals("C1", m.getCode());
        assertEquals("N1", m.getName());
        assertEquals("D1", m.getDescpiption());
        assertTrue(m.getEnabled());
        assertEquals(c, m.getCreatedAt());
        assertEquals(u, m.getUpdatedAt());
        assertEquals("creator", m.getCreatedBy());
        assertEquals("updater", m.getUpdateBy());
    }

    @Test
    void withers_shouldCreateModifiedCopies() {
        ProductTypeModel m = base();

        UUID newId = UUID.randomUUID();
        LocalDateTime newC = m.getCreatedAt().minusDays(1);
        LocalDateTime newU = m.getUpdatedAt().plusDays(1);

        assertEquals(newId, m.withId(newId).getId());
        assertEquals("C2", m.withCode("C2").getCode());
        assertEquals("New Name", m.withName("New Name").getName());
        assertEquals("New Desc", m.withDescpiption("New Desc").getDescpiption());
        assertFalse(m.withEnabled(false).getEnabled());
        assertEquals(newC, m.withCreatedAt(newC).getCreatedAt());
        assertEquals(newU, m.withUpdatedAt(newU).getUpdatedAt());
        assertEquals("newCreator", m.withCreatedBy("newCreator").getCreatedBy());
        assertEquals("newUpdater", m.withUpdateBy("newUpdater").getUpdateBy());
    }

    @Test
    void toBuilder_shouldCopyAndAllowOverride() {
        ProductTypeModel a = base();
        ProductTypeModel b = a.toBuilder().name("Override").build();

        assertNotSame(a, b);
        assertEquals(a.getId(), b.getId());
        assertEquals("Override", b.getName());
        assertEquals(a.getCode(), b.getCode());
    }

    // ----- equals / hashCode branches -----

    @Test
    void equals_shouldBeTrue_forSameInstance() {
        ProductTypeModel a = base();
        assertEquals(a, a);
    }

    @Test
    void equals_shouldBeFalse_againstNull() {
        ProductTypeModel a = base();
        assertNotEquals(null, a);
    }

    @Test
    void equals_shouldBeFalse_againstDifferentClass() {
        ProductTypeModel a = base();
        assertNotEquals("not-a-model", a);
    }

    @Test
    void equalsAndHashCode_shouldBeTrue_whenAllFieldsMatch() {
        ProductTypeModel a = base();
        ProductTypeModel b = a.toBuilder().build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_shouldBeFalse_whenAnyFieldDiffers() {
        ProductTypeModel a = base();
        ProductTypeModel b = a.toBuilder().code("DIFF").build();
        assertNotEquals(a, b);
    }

    @Test
    void toString_shouldContainKeyFields() {
        ProductTypeModel m = ProductTypeModel.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000007"))
                .code("PT-777")
                .name("Visible")
                .build();

        String s = m.toString();
        assertTrue(s.contains("ProductTypeModel"));
        assertTrue(s.contains("PT-777"));
        assertTrue(s.contains("Visible"));
        assertTrue(s.contains("00000000-0000-0000-0000-000000000007"));
    }
}