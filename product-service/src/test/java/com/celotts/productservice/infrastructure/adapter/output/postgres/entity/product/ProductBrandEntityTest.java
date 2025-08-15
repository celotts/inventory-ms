package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandEntityTest {

    @Test
    @DisplayName("Builder debe construir correctamente la entidad")
    void builderShouldBuildEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandEntity entity = ProductBrandEntity.builder()
                .id(id)
                .name("BrandX")
                .description("Desc")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester2")
                .createdAt(now)
                .updatedAt(now.plusMinutes(1))
                .build();

        assertEquals(id, entity.getId());
        assertEquals("BrandX", entity.getName());
        assertEquals("Desc", entity.getDescription());
        assertEquals(true, entity.getEnabled());
        assertEquals("tester", entity.getCreatedBy());
        assertEquals("tester2", entity.getUpdatedBy());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now.plusMinutes(1), entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Getters/Setters funcionan y respetan tipos")
    void gettersSettersWork() {
        ProductBrandEntity entity = new ProductBrandEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = created.plusHours(2);

        entity.setId(id);
        entity.setName("ACME");
        entity.setDescription("Quality brand");
        entity.setEnabled(false);
        entity.setCreatedBy("system");
        entity.setUpdatedBy("admin");
        entity.setCreatedAt(created);
        entity.setUpdatedAt(updated);

        assertAll(
                () -> assertEquals(id, entity.getId()),
                () -> assertEquals("ACME", entity.getName()),
                () -> assertEquals("Quality brand", entity.getDescription()),
                () -> assertFalse(entity.getEnabled()),
                () -> assertEquals("system", entity.getCreatedBy()),
                () -> assertEquals("admin", entity.getUpdatedBy()),
                () -> assertEquals(created, entity.getCreatedAt()),
                () -> assertEquals(updated, entity.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("equals y hashCode consideran todos los campos (lombok @Data)")
    void equalsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime c = LocalDateTime.now();
        LocalDateTime u = c.plusSeconds(30);

        ProductBrandEntity a = ProductBrandEntity.builder()
                .id(id).name("B").description("D").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(c).updatedAt(u)
                .build();

        ProductBrandEntity b = ProductBrandEntity.builder()
                .id(id).name("B").description("D").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(c).updatedAt(u)
                .build();

        ProductBrandEntity cDiff = ProductBrandEntity.builder()
                .id(id).name("B2").description("D").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(c).updatedAt(u)
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, cDiff);
    }

    @Test
    @DisplayName("toString contiene información clave y el nombre de la clase")
    void toStringContainsFields() {
        ProductBrandEntity e = ProductBrandEntity.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("Name")
                .enabled(true)
                .build();

        String s = e.toString();
        assertTrue(s.contains("ProductBrandEntity"));
        assertTrue(s.contains("Name"));
        assertTrue(s.contains("enabled=true"));
    }

    @Test
    @DisplayName("Constructores no-args y all-args están disponibles")
    void constructorsExist() {
        // no-args
        ProductBrandEntity empty = new ProductBrandEntity();
        assertNotNull(empty);

        // all-args
        ProductBrandEntity full = new ProductBrandEntity(
                UUID.randomUUID(),
                "N", "D", true,
                "cby", "uby",
                LocalDateTime.now(), LocalDateTime.now()
        );
        assertNotNull(full);
        assertEquals("N", full.getName());
    }

    @Test
    @DisplayName("Anotaciones JPA: @Entity, @Table y columnas mapeadas")
    void jpaAnnotationsPresent() throws Exception {
        // Clase
        Entity entityAnn = ProductBrandEntity.class.getAnnotation(Entity.class);
        assertNotNull(entityAnn, "@Entity debe estar presente");

        Table tableAnn = ProductBrandEntity.class.getAnnotation(Table.class);
        assertNotNull(tableAnn, "@Table debe estar presente");
        assertEquals("product_brand", tableAnn.name());

        // id
        Field id = ProductBrandEntity.class.getDeclaredField("id");
        assertNotNull(id.getAnnotation(Id.class));
        GeneratedValue gen = id.getAnnotation(GeneratedValue.class);
        assertNotNull(gen);
        assertEquals(GenerationType.AUTO, gen.strategy());

        // columnas con nombre explícito
        assertColumnName("createdBy", "created_by");
        assertColumnName("updatedBy", "updated_by");
        assertColumnName("createdAt", "created_at");
        assertColumnName("updatedAt", "updated_at");
    }

    private void assertColumnName(String fieldName, String expectedColumn) throws NoSuchFieldException {
        Field f = ProductBrandEntity.class.getDeclaredField(fieldName);
        Column c = f.getAnnotation(Column.class);
        assertNotNull(c, () -> "@Column faltante en campo " + fieldName);
        assertEquals(expectedColumn, c.name());
    }
}
