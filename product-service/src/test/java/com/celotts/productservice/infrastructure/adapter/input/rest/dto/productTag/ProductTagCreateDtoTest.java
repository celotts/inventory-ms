package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagCreateDtoTest {

    // ----------- Pruebas simples -----------

    @Test
    void builder_shouldSetFieldsAndApplyDefaultEnabled() {
        ProductTagCreateDto dto = ProductTagCreateDto.builder()
                .name("Etiqueta")
                .description("Descripción de prueba")
                .createdBy("user1")
                .build();

        // Getters
        assertThat(dto.getName()).isEqualTo("Etiqueta");
        assertThat(dto.getDescription()).isEqualTo("Descripción de prueba");
        assertThat(dto.getCreatedBy()).isEqualTo("user1");

        // @Builder.Default -> TRUE cuando no se establece
        assertThat(dto.getEnabled()).isTrue();
    }

    @Test
    void equalsAndHashCode_shouldBeBasedOnAllFields() {
        ProductTagCreateDto a = ProductTagCreateDto.builder()
                .name("Etiqueta").description("Desc").enabled(true).createdBy("user1").build();
        ProductTagCreateDto b = ProductTagCreateDto.builder()
                .name("Etiqueta").description("Desc").enabled(true).createdBy("user1").build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        // y, por sanidad, referencias distintas
        assertThat(a).isNotSameAs(b);
    }

    @Test
    void toString_shouldContainFieldValues() {
        ProductTagCreateDto dto = ProductTagCreateDto.builder()
                .name("Etiqueta")
                .description("Desc")
                .enabled(true)
                .createdBy("user1")
                .build();

        assertThat(dto.toString()).contains("Etiqueta", "Desc", "user1");
    }

    // ----------- Pruebas PARAMETRIZADAS -----------

    @ParameterizedTest(name = "enabled explícito = {0} -> esperado en DTO = {1}")
    @MethodSource("enabledCases")
    void builder_shouldRespectEnabledOrUseDefault(Boolean explicitEnabled, boolean expected) {
        ProductTagCreateDto.ProductTagCreateDtoBuilder builder = ProductTagCreateDto.builder()
                .name("Etiqueta")
                .description("x")
                .createdBy("user");

        if (explicitEnabled != null) {
            builder.enabled(explicitEnabled);
        }
        ProductTagCreateDto dto = builder.build();

        assertThat(dto.getEnabled()).isEqualTo(expected);
    }

    private static Stream<Arguments> enabledCases() {
        return Stream.of(
                // null => no se establece -> @Builder.Default TRUE
                Arguments.of(null, true),
                Arguments.of(Boolean.TRUE, true),
                Arguments.of(Boolean.FALSE, false)
        );
    }

    @ParameterizedTest(name = "Deben NO ser iguales si cambia {0}")
    @MethodSource("inequalityCases")
    void equals_shouldBeFalse_whenAnyFieldDiffers(ProductTagCreateDto a, ProductTagCreateDto b) {
        assertThat(a).isNotEqualTo(b);
        // hashCode no está obligado, pero normalmente cambia:
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    private static Stream<Arguments> inequalityCases() {
        ProductTagCreateDto base = ProductTagCreateDto.builder()
                .name("Etiqueta").description("Desc").enabled(true).createdBy("user").build();

        return Stream.of(
                Arguments.of(base,
                        ProductTagCreateDto.builder().name("Otra").description("Desc").enabled(true).createdBy("user").build()),
                Arguments.of(base,
                        ProductTagCreateDto.builder().name("Etiqueta").description("Otro").enabled(true).createdBy("user").build()),
                Arguments.of(base,
                        ProductTagCreateDto.builder().name("Etiqueta").description("Desc").enabled(false).createdBy("user").build()),
                Arguments.of(base,
                        ProductTagCreateDto.builder().name("Etiqueta").description("Desc").enabled(true).createdBy("otro").build())
        );
    }

    @ParameterizedTest(name = "toString() debe contener \"{0}\"")
    @MethodSource("toStringPieces")
    void toString_shouldContainPieces(String name, String desc, String createdBy) {
        ProductTagCreateDto dto = ProductTagCreateDto.builder()
                .name(name).description(desc).createdBy(createdBy).enabled(true).build();

        assertThat(dto.toString()).contains(name, desc, createdBy);
    }

    private static Stream<Arguments> toStringPieces() {
        return Stream.of(
                Arguments.of("Etiqueta", "Desc", "user"),
                Arguments.of("Tag 2", "Otro texto", "admin")
        );
    }
}