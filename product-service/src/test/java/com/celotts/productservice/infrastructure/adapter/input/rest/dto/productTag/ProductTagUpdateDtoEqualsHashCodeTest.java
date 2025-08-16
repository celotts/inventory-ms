package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagUpdateDtoEqualsHashCodeTest {

    private ProductTagUpdateDto dto(String name, String desc, Boolean enabled, String updatedBy) {
        return ProductTagUpdateDto.builder()
                .name(name)
                .description(desc)
                .enabled(enabled)
                .updatedBy(updatedBy)
                .build();
    }

    @Test
    void equals_shouldBeTrue_whenAllFieldsMatch() {
        ProductTagUpdateDto a = dto("Etiqueta", "Desc", true, "user");
        ProductTagUpdateDto b = dto("Etiqueta", "Desc", true, "user");

        // no es la misma referencia
        assertThat(a).isNotSameAs(b);
        // pero son iguales por valor
        assertThat(a).isEqualTo(b);
        // y su hashCode coincide
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void equals_shouldBeFalse_whenNameDiffers() {
        ProductTagUpdateDto a = dto("Etiqueta", "Desc", true, "user");
        ProductTagUpdateDto b = dto("Otra", "Desc", true, "user");

        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    @Test
    void equals_shouldBeFalse_whenDescriptionDiffers() {
        ProductTagUpdateDto a = dto("Etiqueta", "Desc A", true, "user");
        ProductTagUpdateDto b = dto("Etiqueta", "Desc B", true, "user");

        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    @Test
    void equals_shouldBeFalse_whenEnabledDiffers() {
        ProductTagUpdateDto a = dto("Etiqueta", "Desc", true, "user");
        ProductTagUpdateDto b = dto("Etiqueta", "Desc", false, "user");

        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    @Test
    void equals_shouldBeFalse_whenUpdatedByDiffers() {
        ProductTagUpdateDto a = dto("Etiqueta", "Desc", true, "userA");
        ProductTagUpdateDto b = dto("Etiqueta", "Desc", true, "userB");

        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    @Test
    void equals_shouldHandleNullAndDifferentClass() {
        ProductTagUpdateDto a = dto("Etiqueta", "Desc", true, "user");

        assertThat(a.equals(null)).isFalse();           // null
        assertThat(a.equals("no-es-dto")).isFalse();    // clase distinta
        assertThat(a).isEqualTo(a);                     // reflexividad
    }
}