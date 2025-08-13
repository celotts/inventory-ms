package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagResponseDtoTest {

    private ProductTagResponseDto buildSample(UUID id, String name,
                                              String desc, Boolean enabled,
                                              String createdBy, String updatedBy,
                                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        return ProductTagResponseDto.builder()
                .id(id)
                .name(name)
                .description(desc)
                .enabled(enabled)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    void builder_shouldBuild_andExposeAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime c = LocalDateTime.of(2025, 1, 1, 10, 20, 30);
        LocalDateTime u = LocalDateTime.of(2025, 1, 2, 11, 22, 33);

        ProductTagResponseDto dto = buildSample(
                id, "Etiqueta", "Desc", true, "creator", "updater", c, u
        );

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("Etiqueta");
        assertThat(dto.getDescription()).isEqualTo("Desc");
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getCreatedBy()).isEqualTo("creator");
        assertThat(dto.getUpdatedBy()).isEqualTo("updater");
        assertThat(dto.getCreatedAt()).isEqualTo(c);
        assertThat(dto.getUpdatedAt()).isEqualTo(u);
    }

    @Test
    void equals_and_hashCode_true_whenAllFieldsMatch() {
        LocalDateTime c = LocalDateTime.of(2025, 1, 1, 10, 20, 30);
        LocalDateTime u = LocalDateTime.of(2025, 1, 2, 11, 22, 33);
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        ProductTagResponseDto a = buildSample(id, "A", "D", true, "c", "u", c, u);
        ProductTagResponseDto b = buildSample(id, "A", "D", true, "c", "u", c, u);

        assertThat(a).isNotSameAs(b);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        // Consistencia de hashCode
        int h1 = a.hashCode();
        int h2 = a.hashCode();
        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void equals_false_whenAnyFieldDiffers() {
        LocalDateTime c = LocalDateTime.of(2025, 1, 1, 10, 20, 30);
        LocalDateTime u = LocalDateTime.of(2025, 1, 2, 11, 22, 33);

        ProductTagResponseDto base = buildSample(
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "Etiqueta", "Desc", true, "creator", "updater", c, u
        );

        ProductTagResponseDto diffId = buildSample(
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "Etiqueta", "Desc", true, "creator", "updater", c, u
        );
        ProductTagResponseDto diffName = buildSample(
                base.getId(), "Otro", "Desc", true, "creator", "updater", c, u
        );

        assertThat(base).isNotEqualTo(diffId);
        assertThat(base).isNotEqualTo(diffName);
        assertThat(base).isNotEqualTo(null);
        assertThat(base).isNotEqualTo("not-a-dto");
    }

    @Test
    void toString_containsKeyData() {
        ProductTagResponseDto dto = buildSample(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Etiqueta", "Desc", true, "creator", "updater",
                LocalDateTime.of(2025, 1, 1, 10, 20, 30),
                LocalDateTime.of(2025, 1, 2, 11, 22, 33)
        );

        String s1 = dto.toString();
        String s2 = dto.toString(); // determinista

        assertThat(s1).contains("Etiqueta").contains("creator");
        assertThat(s2).isEqualTo(s1);
    }

    @Test
    void jsonSerialization_usesConfiguredDatePattern() throws JsonProcessingException {
        LocalDateTime created = LocalDateTime.of(2025, 3, 4, 5, 6, 7);
        LocalDateTime updated = LocalDateTime.of(2025, 3, 5, 6, 7, 8);

        ProductTagResponseDto dto = buildSample(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Nombre", "D", true, "c", "u", created, updated
        );

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = mapper.writeValueAsString(dto);

        // Debe respetar el patrón "yyyy-MM-dd HH:mm:ss"
        assertThat(json).contains("\"createdAt\":\"2025-03-04 05:06:07\"");
        assertThat(json).contains("\"updatedAt\":\"2025-03-05 06:07:08\"");
    }
}