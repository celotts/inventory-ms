package com.celotts.productservice.domain.model;

import com.celotts.productserviceOld.domain.model.ProductTagModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagModelTest {

    private ProductTagModel base() {
        return ProductTagModel.builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .name("Tag")
                .description("Desc")
                .enabled(false)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(LocalDateTime.of(2025, 1, 1, 10, 20, 30))
                .updatedAt(LocalDateTime.of(2025, 1, 2, 11, 22, 33))
                .build();
    }

    @Test
    void withEnabled_shouldReturnNewInstance_withUpdatedFlag_andKeepOtherFields() {
        ProductTagModel original = base();

        ProductTagModel toggled = original.withEnabled(true);

        // Nueva instancia (inmutabilidad)
        assertThat(toggled).isNotSameAs(original);

        // Cambia solo 'enabled'
        assertThat(toggled.getEnabled()).isTrue();
        assertThat(original.getEnabled()).isFalse();

        // El resto de campos se mantiene igual
        assertThat(toggled)
                .usingRecursiveComparison()
                .ignoringFields("enabled")
                .isEqualTo(original);
    }

    @Test
    void withEnabled_canToggleBack_andEqualsOriginal() {
        ProductTagModel original = base();

        ProductTagModel back = original.withEnabled(true).withEnabled(false);

        // Al volver a false, debe ser igual por valor al original
        assertThat(back).isEqualTo(original);
        assertThat(back.hashCode()).isEqualTo(original.hashCode());
    }

    @Test
    void toString_shouldContainClassNameAndFields() {
        ProductTagModel model = base();

        String str = model.toString();

        // Validaciones básicas
        assertThat(str).isNotNull();
        assertThat(str).contains("ProductTagModel");
        assertThat(str).contains("Tag");
        assertThat(str).contains("Desc");
        assertThat(str).contains("false"); // estado inicial de enabled
    }
}