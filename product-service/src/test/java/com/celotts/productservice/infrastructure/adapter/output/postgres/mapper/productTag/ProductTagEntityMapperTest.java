package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.productTag;

import com.celotts.productserviceOld.domain.model.ProductTagModel;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductTagEntityMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagEntityMapperTest {

    private final ProductTagEntityMapper mapper = new ProductTagEntityMapper();

    @Test
    void toDomain_maps_all_fields() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now().minusHours(3);

        ProductTagEntity e = ProductTagEntity.builder()
                .id(id)
                .name("Gaming")
                .description("Gaming stuff")
                .enabled(true)
                .createdAt(created)
                .updatedAt(updated)
                .createdBy("alice")
                .updatedBy("bob")
                .build();

        ProductTagModel m = mapper.toDomain(e);

        assertThat(m).isNotNull();
        assertThat(m.getId()).isEqualTo(id);
        assertThat(m.getName()).isEqualTo("Gaming");
        assertThat(m.getDescription()).isEqualTo("Gaming stuff");
        assertThat(m.getEnabled()).isTrue();
        assertThat(m.getCreatedAt()).isEqualTo(created);
        assertThat(m.getUpdatedAt()).isEqualTo(updated);
        assertThat(m.getCreatedBy()).isEqualTo("alice");
        assertThat(m.getUpdatedBy()).isEqualTo("bob");
    }

    @Test
    void toEntity_maps_all_fields() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(5);
        LocalDateTime updated = LocalDateTime.now().minusDays(1);

        ProductTagModel m = ProductTagModel.builder()
                .id(id)
                .name("Tech")
                .description("Tech desc")
                .enabled(false)
                .createdAt(created)
                .updatedAt(updated)
                .createdBy("carol")
                .updatedBy("dave")
                .build();

        ProductTagEntity e = mapper.toEntity(m);

        assertThat(e).isNotNull();
        assertThat(e.getId()).isEqualTo(id);
        assertThat(e.getName()).isEqualTo("Tech");
        assertThat(e.getDescription()).isEqualTo("Tech desc");
        assertThat(e.getEnabled()).isFalse();
        assertThat(e.getCreatedAt()).isEqualTo(created);
        assertThat(e.getUpdatedAt()).isEqualTo(updated);
        assertThat(e.getCreatedBy()).isEqualTo("carol");
        assertThat(e.getUpdatedBy()).isEqualTo("dave");
    }

    @Test
    void updateEntity_updates_mutable_fields_and_keeps_immutable() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(10);

        ProductTagEntity target = ProductTagEntity.builder()
                .id(id)
                .name("Old")
                .description("Old desc")
                .enabled(false)
                .createdAt(created)
                .updatedAt(LocalDateTime.now().minusDays(5))
                .createdBy("creator")
                .updatedBy("old-updater")
                .build();

        ProductTagModel source = ProductTagModel.builder()
                .name("New")
                .description("New desc")
                .enabled(true)
                .updatedBy("new-updater")
                .updatedAt(LocalDateTime.now())
                .build();

        mapper.updateEntity(target, source);

        // cambiados
        assertThat(target.getName()).isEqualTo("New");
        assertThat(target.getDescription()).isEqualTo("New desc");
        assertThat(target.getEnabled()).isTrue();
        assertThat(target.getUpdatedBy()).isEqualTo("new-updater");
        assertThat(target.getUpdatedAt()).isEqualTo(source.getUpdatedAt());

        // intactos
        assertThat(target.getId()).isEqualTo(id);
        assertThat(target.getCreatedAt()).isEqualTo(created);
        assertThat(target.getCreatedBy()).isEqualTo("creator");
    }

    @Test
    void null_inputs_are_handled_gracefully() {
        assertThat(mapper.toDomain(null)).isNull();
        assertThat(mapper.toEntity(null)).isNull();

        // updateEntity: si alguno es null no debe lanzar excepción ni cambiar nada
        ProductTagEntity target = ProductTagEntity.builder()
                .name("keep").description("keep").enabled(true).build();

        mapper.updateEntity(null, ProductTagModel.builder().build()); // no exception
        mapper.updateEntity(target, null); // no exception

        assertThat(target.getName()).isEqualTo("keep");
        assertThat(target.getDescription()).isEqualTo("keep");
        assertThat(target.getEnabled()).isTrue();
    }
}