package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitEntityTest {

    @Test
    void testSettersAndToString() {
        ProductUnitEntity unit = new ProductUnitEntity();
        UUID id = UUID.randomUUID();
        String code = "U001";
        String name = "Unidad";
        String description = "Unidad de peso";
        String symbol = "kg";
        Boolean enabled = true;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "editor";

        unit.setId(id);
        unit.setCode(code);
        unit.setName(name);
        unit.setDescription(description);
        unit.setSymbol(symbol);
        unit.setEnabled(enabled);
        unit.setCreatedAt(createdAt);
        unit.setUpdatedAt(updatedAt);
        unit.setCreatedBy(createdBy);
        unit.setUpdatedBy(updatedBy);

        assertEquals(id, unit.getId());
        assertEquals(code, unit.getCode());
        assertEquals(name, unit.getName());
        assertEquals(description, unit.getDescription());
        assertEquals(symbol, unit.getSymbol());
        assertEquals(enabled, unit.getEnabled());
        assertEquals(createdAt, unit.getCreatedAt());
        assertEquals(updatedAt, unit.getUpdatedAt());
        assertEquals(createdBy, unit.getCreatedBy());
        assertEquals(updatedBy, unit.getUpdatedBy());

        String toString = unit.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("U001"));
        assertTrue(toString.contains("Unidad"));
        assertTrue(toString.contains("kg"));
    }

    @Test
    void testEqualsHashCodeCanEqual() {
        UUID id = UUID.randomUUID();

        ProductUnitEntity u1 = ProductUnitEntity.builder()
                .id(id)
                .code("U100")
                .name("Unidad A")
                .build();

        ProductUnitEntity u2 = ProductUnitEntity.builder()
                .id(id)
                .code("U100")
                .name("Unidad A")
                .build();

        ProductUnitEntity u3 = ProductUnitEntity.builder()
                .id(UUID.randomUUID())
                .code("U200")
                .name("Unidad B")
                .build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1, u3);
        assertNotEquals(u1.hashCode(), u3.hashCode());

        assertTrue(u1.canEqual(u2));
        assertFalse(u1.canEqual("String"));
    }

    @Test
    void testJpaAnnotationsPresent() throws Exception {
        // Clase
        Entity entityAnn = ProductUnitEntity.class.getAnnotation(Entity.class);
        assertNotNull(entityAnn, "@Entity debe estar presente");

        Table tableAnn = ProductUnitEntity.class.getAnnotation(Table.class);
        assertNotNull(tableAnn, "@Table debe estar presente");
        assertEquals("product_unit", tableAnn.name());

        // id
        Field idF = ProductUnitEntity.class.getDeclaredField("id");
        assertNotNull(idF.getAnnotation(Id.class), "@Id faltante en id");
        assertNotNull(idF.getAnnotation(GeneratedValue.class), "@GeneratedValue faltante en id");

        // code: unique = true, nullable = false
        Field codeF = ProductUnitEntity.class.getDeclaredField("code");
        Column codeCol = codeF.getAnnotation(Column.class);
        assertNotNull(codeCol, "@Column faltante en code");
        assertTrue(codeCol.unique(), "code debe ser unique=true");
        assertFalse(codeCol.nullable(), "code debe ser nullable=false");

        // symbol: nullable = false
        Field symbolF = ProductUnitEntity.class.getDeclaredField("symbol");
        Column symbolCol = symbolF.getAnnotation(Column.class);
        assertNotNull(symbolCol, "@Column faltante en symbol");
        assertFalse(symbolCol.nullable(), "symbol debe ser nullable=false");
    }
    @Test
    void testBuilderAllFields() {
        UUID id = UUID.randomUUID();
        String code = "U777";
        String name = "Unidad Completa";
        String description = "Entidad con todos los campos";
        String symbol = "u";
        Boolean enabled = Boolean.TRUE;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "creator";
        String updatedBy = "upd";

        ProductUnitEntity unit = ProductUnitEntity.builder()
                .id(id)
                .code(code)
                .name(name)
                .description(description)
                .symbol(symbol)
                .enabled(enabled)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();

        assertAll(
                () -> assertEquals(id, unit.getId()),
                () -> assertEquals(code, unit.getCode()),
                () -> assertEquals(name, unit.getName()),
                () -> assertEquals(description, unit.getDescription()),
                () -> assertEquals(symbol, unit.getSymbol()),
                () -> assertEquals(enabled, unit.getEnabled()),
                () -> assertEquals(createdAt, unit.getCreatedAt()),
                () -> assertEquals(updatedAt, unit.getUpdatedAt()),
                () -> assertEquals(createdBy, unit.getCreatedBy()),
                () -> assertEquals(updatedBy, unit.getUpdatedBy())
        );
    }

    @Test
    void testEqualsHashCodeSensitiveToOtherFields() {
        UUID id = UUID.randomUUID();

        ProductUnitEntity a = ProductUnitEntity.builder()
                .id(id).code("U900").name("Unidad X").description("D1").symbol("kg").build();

        // Igual a 'a'
        ProductUnitEntity b = ProductUnitEntity.builder()
                .id(id).code("U900").name("Unidad X").description("D1").symbol("kg").build();

        // Cambia 'symbol' (debería romper igualdad por @Data usa todos los campos)
        ProductUnitEntity cDiffSymbol = ProductUnitEntity.builder()
                .id(id).code("U900").name("Unidad X").description("D1").symbol("lb").build();

        // Cambia 'description'
        ProductUnitEntity dDiffDesc = ProductUnitEntity.builder()
                .id(id).code("U900").name("Unidad X").description("D2").symbol("kg").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, cDiffSymbol);
        assertNotEquals(a.hashCode(), cDiffSymbol.hashCode());

        assertNotEquals(a, dDiffDesc);
        assertNotEquals(a.hashCode(), dDiffDesc.hashCode());
    }

    @Test
    void testEnabledAllowsNull() {
        ProductUnitEntity unit = new ProductUnitEntity();
        unit.setEnabled(null);
        assertNull(unit.getEnabled(), "enabled puede ser null (no hay validación en entidad)");
    }


}