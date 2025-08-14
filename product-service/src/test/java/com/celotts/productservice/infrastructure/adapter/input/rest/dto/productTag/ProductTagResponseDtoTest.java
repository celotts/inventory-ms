package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagResponseDtoTest {

    private static ProductTagResponseDto sample(UUID id, String name,
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
    void builder_builds_and_exposes_fields() {
        UUID id = UUID.randomUUID();
        LocalDateTime c = LocalDateTime.of(2025, 1, 1, 10, 20, 30);
        LocalDateTime u = LocalDateTime.of(2025, 1, 2, 11, 22, 33);

        ProductTagResponseDto dto = sample(id, "Etiqueta", "Desc",
                true, "creator", "updater", c, u);

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
    void equals_and_hashCode_match_when_all_fields_match() {
        UUID id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        LocalDateTime c = LocalDateTime.of(2025, 1, 1, 10, 20, 30);
        LocalDateTime u = LocalDateTime.of(2025, 1, 2, 11, 22, 33);

        ProductTagResponseDto a = sample(id, "A", "D", true, "c", "u", c, u);
        ProductTagResponseDto b = sample(id, "A", "D", true, "c", "u", c, u);

        assertThat(a).isNotSameAs(b);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.hashCode()).isEqualTo(a.hashCode());
    }

    @Test
    void equals_returns_false_for_different_fields_or_types_or_null() {
        UUID id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        LocalDateTime c = LocalDateTime.of(2025, 1, 1, 10, 20, 30);
        LocalDateTime u = LocalDateTime.of(2025, 1, 2, 11, 22, 33);

        ProductTagResponseDto base = sample(id, "Etiqueta", "Desc", true, "creator", "updater", c, u);
        ProductTagResponseDto diffId = sample(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "Etiqueta", "Desc", true, "creator", "updater", c, u);
        ProductTagResponseDto diffName = sample(id, "Otro", "Desc", true, "creator", "updater", c, u);

        assertThat(base).isNotEqualTo(diffId);
        assertThat(base).isNotEqualTo(diffName);
        assertThat(base).isNotEqualTo(null);
        assertThat(base).isNotEqualTo("not-a-dto");
    }

    @Test
    void toString_is_deterministic_and_contains_key_data() {
        ProductTagResponseDto dto = sample(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Etiqueta", "Desc", true, "creator", "updater",
                LocalDateTime.of(2025, 1, 1, 10, 20, 30),
                LocalDateTime.of(2025, 1, 2, 11, 22, 33)
        );

        String s1 = dto.toString();
        String s2 = dto.toString();

        assertThat(s1).contains("Etiqueta").contains("creator");
        assertThat(s2).isEqualTo(s1);
    }

    @Test
    void jsonSerialization_respects_date_pattern() throws JsonProcessingException {
        LocalDateTime created = LocalDateTime.of(2025, 3, 4, 5, 6, 7);
        LocalDateTime updated = LocalDateTime.of(2025, 3, 5, 6, 7, 8);

        ProductTagResponseDto dto = sample(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Nombre", "D", true, "c", "u", created, updated
        );

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = mapper.writeValueAsString(dto);

        assertThat(json).contains("\"createdAt\":\"2025-03-04 05:06:07\"");
        assertThat(json).contains("\"updatedAt\":\"2025-03-05 06:07:08\"");
    }

    @Test
    void jsonSerialization_with_null_dates_serializes_nulls() throws JsonProcessingException {
        ProductTagResponseDto dto = sample(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "N", "D", true, "c", "u", null, null
        );

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = mapper.writeValueAsString(dto);

        assertThat(json).contains("\"createdAt\":null");
        assertThat(json).contains("\"updatedAt\":null");
    }

    @Test
    void equals_works_with_null_optional_fields() {
        UUID id = UUID.fromString("44444444-4444-4444-4444-444444444444");

        ProductTagResponseDto a = sample(id, null, null, null, null, null, null, null);
        ProductTagResponseDto b = sample(id, null, null, null, null, null, null, null);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}