package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryResponseDtoTest {

    @Test
    void builder_callsEveryMethod_andBuilds() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Llamamos a TODOS los métodos del builder para cubrir la clase interna *Builder
        ProductCategoryResponseDto dto = ProductCategoryResponseDto.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .enabled(Boolean.TRUE)
                .updatedBy("upd")
                .createdBy("creator")
                .assignedAt(now)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getCategoryId()).isEqualTo(categoryId);
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getUpdatedBy()).isEqualTo("upd");
        assertThat(dto.getCreatedBy()).isEqualTo("creator");
        assertThat(dto.getAssignedAt()).isEqualTo(now);
        assertThat(dto.getCreatedAt()).isEqualTo(now.minusDays(1));
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void noArgsAndSetters_equalsHashCode_andToString() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime a1 = LocalDateTime.now().minusDays(2);
        LocalDateTime c1 = LocalDateTime.now().minusDays(1);
        LocalDateTime u1 = LocalDateTime.now();

        ProductCategoryResponseDto a = new ProductCategoryResponseDto();
        a.setId(id);
        a.setProductId(productId);
        a.setCategoryId(categoryId);
        a.setEnabled(false);
        a.setUpdatedBy("upd");
        a.setCreatedBy("creator");
        a.setAssignedAt(a1);
        a.setCreatedAt(c1);
        a.setUpdatedAt(u1);

        ProductCategoryResponseDto b = ProductCategoryResponseDto.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .enabled(false)
                .updatedBy("upd")
                .createdBy("creator")
                .assignedAt(a1)
                .createdAt(c1)
                .updatedAt(u1)
                .build();

        // equals/hashCode (iguales)
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        // equals: misma instancia / null / otra clase
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other");

        // equals: diferente en un campo
        ProductCategoryResponseDto c = ProductCategoryResponseDto.builder()
                .id(UUID.randomUUID()) // solo cambia el id
                .productId(productId)
                .categoryId(categoryId)
                .enabled(false)
                .updatedBy("upd")
                .createdBy("creator")
                .assignedAt(a1)
                .createdAt(c1)
                .updatedAt(u1)
                .build();
        assertThat(a).isNotEqualTo(c);

        // toString contiene valores representativos
        String ts = a.toString();
        assertThat(ts).contains("creator").contains("upd");
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assignedAt = LocalDateTime.now().minusDays(3);
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);

        ProductCategoryResponseDto dto = new ProductCategoryResponseDto(
                id, productId, categoryId, true, "upd", "creator",
                assignedAt, createdAt, updatedAt
        );

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getCategoryId()).isEqualTo(categoryId);
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getUpdatedBy()).isEqualTo("upd");
        assertThat(dto.getCreatedBy()).isEqualTo("creator");
        assertThat(dto.getAssignedAt()).isEqualTo(assignedAt);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void builder_toString_covers_inner_builder() {
        String s = ProductCategoryResponseDto.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .enabled(true)
                .updatedBy("upd")
                .createdBy("creator")
                .assignedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .toString(); // toString del *builder*

        assertThat(s).isNotNull().isNotBlank();
        // si tu Lombok incluye el nombre del builder:
        assertThat(s).contains("ProductCategoryResponseDtoBuilder");
    }
}