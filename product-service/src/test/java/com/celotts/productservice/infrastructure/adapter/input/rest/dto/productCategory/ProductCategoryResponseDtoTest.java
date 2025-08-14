package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
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

    private ProductCategoryResponseDto fullDto() {
        return ProductCategoryResponseDto.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .productId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .categoryId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                .enabled(Boolean.TRUE)
                .updatedBy("updater")
                .createdBy("creator")
                .assignedAt(LocalDateTime.of(2025, 1, 2, 3, 4, 5))
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 3, 0, 0, 0))
                .build();
    }

    @Test
    void equals_isReflexive_andHashCodeConsistent() {
        ProductCategoryResponseDto a = fullDto();

        // Reflexivo
        assertThat(a).isEqualTo(a);

        // Consistencia de hashCode en múltiples llamadas
        int h1 = a.hashCode();
        int h2 = a.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void equals_true_andHashCodesMatch_whenAllFieldsMatch() {
        ProductCategoryResponseDto a = fullDto();
        ProductCategoryResponseDto b = fullDto();

        // No es la misma referencia
        assertThat(a).isNotSameAs(b);

        // Pero son iguales por valor y con mismo hash
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equals_false_whenAnyFieldDiffers() {
        ProductCategoryResponseDto base = fullDto();

        // Caso 1: cambia el id
        ProductCategoryResponseDto differentId = ProductCategoryResponseDto.builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .productId(base.getProductId())
                .categoryId(base.getCategoryId())
                .enabled(base.getEnabled())
                .updatedBy(base.getUpdatedBy())
                .createdBy(base.getCreatedBy())
                .assignedAt(base.getAssignedAt())
                .createdAt(base.getCreatedAt())
                .updatedAt(base.getUpdatedAt())
                .build();
        assertThat(base).isNotEqualTo(differentId);

        // Caso 2: cambia enabled
        ProductCategoryResponseDto differentEnabled = ProductCategoryResponseDto.builder()
                .id(base.getId())
                .productId(base.getProductId())
                .categoryId(base.getCategoryId())
                .enabled(Boolean.FALSE) // distinto
                .updatedBy(base.getUpdatedBy())
                .createdBy(base.getCreatedBy())
                .assignedAt(base.getAssignedAt())
                .createdAt(base.getCreatedAt())
                .updatedAt(base.getUpdatedAt())
                .build();
        assertThat(base).isNotEqualTo(differentEnabled);

        // Caso 3: cambia updatedAt
        ProductCategoryResponseDto differentUpdatedAt = ProductCategoryResponseDto.builder()
                .id(base.getId())
                .productId(base.getProductId())
                .categoryId(base.getCategoryId())
                .enabled(base.getEnabled())
                .updatedBy(base.getUpdatedBy())
                .createdBy(base.getCreatedBy())
                .assignedAt(base.getAssignedAt())
                .createdAt(base.getCreatedAt())
                .updatedAt(base.getUpdatedAt().plusDays(1)) // distinto
                .build();
        assertThat(base).isNotEqualTo(differentUpdatedAt);
    }

    @Test
    void equals_false_whenNullOrDifferentType() {
        ProductCategoryResponseDto a = fullDto();

        assertThat(a.equals(null)).isFalse();            // con null
        assertThat(a.equals("not-a-dto")).isFalse();     // con otro tipo
    }
}