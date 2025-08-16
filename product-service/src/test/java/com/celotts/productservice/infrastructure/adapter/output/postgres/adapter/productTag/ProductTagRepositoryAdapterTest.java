package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.productTag;

import com.celotts.productserviceOld.domain.model.ProductTagModel;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.productTag.ProductTagRepositoryAdapter;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductTagEntityMapper;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product.ProductTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTagRepositoryAdapterTest {

    @Mock private ProductTagRepository jpa;
    @Mock private ProductTagEntityMapper mapper;

    private ProductTagRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductTagRepositoryAdapter(jpa, mapper);
    }

    @Test
    void save_mapsModelToEntity_thenMapsSavedEntityToDomain() {
        UUID id = UUID.randomUUID();
        ProductTagModel model = ProductTagModel.builder().id(id).name("Gaming").build();
        ProductTagEntity entity = ProductTagEntity.builder().id(id).name("Gaming").build();
        ProductTagEntity savedEntity = ProductTagEntity.builder().id(id).name("Gaming").build();
        ProductTagModel mappedBack = ProductTagModel.builder().id(id).name("Gaming").build();

        when(mapper.toEntity(model)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(mappedBack);

        ProductTagModel result = adapter.save(model);

        assertThat(result).isSameAs(mappedBack);
        verify(mapper).toEntity(model);
        verify(jpa).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void deleteById_delegatesToJpa() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id);

        verify(jpa).deleteById(id);
    }

    @Test
    void findById_whenPresent_mapsEntityToDomain() {
        UUID id = UUID.randomUUID();
        ProductTagEntity entity = ProductTagEntity.builder().id(id).name("Tech").build();
        ProductTagModel model = ProductTagModel.builder().id(id).name("Tech").build();

        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(model);

        Optional<ProductTagModel> result = adapter.findById(id);

        assertThat(result).contains(model);
        verify(jpa).findById(id);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_whenEmpty_returnsEmpty() {
        UUID id = UUID.randomUUID();
        when(jpa.findById(id)).thenReturn(Optional.empty());

        Optional<ProductTagModel> result = adapter.findById(id);

        assertThat(result).isEmpty();
        verify(jpa).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void findByName_mapsEntityToDomain() {
        String name = "Office";
        ProductTagEntity entity = ProductTagEntity.builder().id(UUID.randomUUID()).name(name).build();
        ProductTagModel model = ProductTagModel.builder().id(entity.getId()).name(name).build();

        when(jpa.findByName(name)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(model);

        Optional<ProductTagModel> result = adapter.findByName(name);

        assertThat(result).contains(model);
        verify(jpa).findByName(name);
        verify(mapper).toDomain(entity);
    }

    @Test
    void existsByName_delegatesToJpa() {
        when(jpa.existsByName("Gaming")).thenReturn(true);

        boolean exists = adapter.existsByName("Gaming");

        assertThat(exists).isTrue();
        verify(jpa).existsByName("Gaming");
    }

    @Test
    void findAll_mapsPageOfEntitiesToPageOfModels() {
        ProductTagEntity e1 = ProductTagEntity.builder().id(UUID.randomUUID()).name("A").build();
        ProductTagEntity e2 = ProductTagEntity.builder().id(UUID.randomUUID()).name("B").build();
        Page<ProductTagEntity> pageEntities = new PageImpl<>(List.of(e1, e2), PageRequest.of(0, 2), 2);

        ProductTagModel m1 = ProductTagModel.builder().id(e1.getId()).name("A").build();
        ProductTagModel m2 = ProductTagModel.builder().id(e2.getId()).name("B").build();

        when(jpa.findAll(any(Pageable.class))).thenReturn(pageEntities);
        when(mapper.toDomain(e1)).thenReturn(m1);
        when(mapper.toDomain(e2)).thenReturn(m2);

        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());
        Page<ProductTagModel> result = adapter.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).containsExactly(m1, m2);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(jpa).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(2);
        verify(mapper).toDomain(e1);
        verify(mapper).toDomain(e2);
    }

    @Test
    void findByEnabled_mapsListEntitiesToModels() {
        ProductTagEntity e1 = ProductTagEntity.builder().id(UUID.randomUUID()).name("X").enabled(true).build();
        ProductTagEntity e2 = ProductTagEntity.builder().id(UUID.randomUUID()).name("Y").enabled(true).build();
        when(jpa.findByEnabled(true)).thenReturn(List.of(e1, e2));

        ProductTagModel m1 = ProductTagModel.builder().id(e1.getId()).name("X").enabled(true).build();
        ProductTagModel m2 = ProductTagModel.builder().id(e2.getId()).name("Y").enabled(true).build();
        when(mapper.toDomain(e1)).thenReturn(m1);
        when(mapper.toDomain(e2)).thenReturn(m2);

        List<ProductTagModel> result = adapter.findByEnabled(true);

        assertThat(result).containsExactly(m1, m2);
        verify(jpa).findByEnabled(true);
        verify(mapper).toDomain(e1);
        verify(mapper).toDomain(e2);
    }

    @Test
    void countByEnabled_delegatesToJpa() {
        when(jpa.countByEnabled(false)).thenReturn(7L);

        long count = adapter.countByEnabled(false);

        assertThat(count).isEqualTo(7L);
        verify(jpa).countByEnabled(false);
    }
}