package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductTypeModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductTypeEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductTypeRepositoryAdapterTest {

    @Mock private ProductTypeJpaRepository jpaRepo;
    @Mock private ProductTypeEntityMapper mapper;

    @InjectMocks
    private ProductTypeRepositoryAdapter adapter;

    @Test
    void existsByCode_returnsTrueWhenRepoSaysSo() {
        when(jpaRepo.existsByCode("TYPE001")).thenReturn(true);
        assertThat(adapter.existsByCode("TYPE001")).isTrue();
    }

    @Test
    void findNameByCode_returnsName() {
        when(jpaRepo.findNameByCode("TYPE001")).thenReturn(Optional.of("Electrónica"));
        assertThat(adapter.findNameByCode("TYPE001")).contains("Electrónica");
    }

    @Test
    void findAllCodes_returnsList() {
        when(jpaRepo.findAllCodes()).thenReturn(List.of("TYPE001", "TYPE002"));
        assertThat(adapter.findAllCodes()).containsExactly("TYPE001", "TYPE002");
    }

    @Test
    void findByCode_mapsEntityToModel() {
        var entity = new ProductTypeEntity();
        entity.setCode("TYPE001");
        entity.setName("Electrónica");

        var model = ProductTypeModel.builder().code("TYPE001").name("Electrónica").build();

        when(jpaRepo.findByCode("TYPE001")).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        assertThat(adapter.findByCode("TYPE001")).contains(model);
    }

    @Test
    void findById_usesUuidToStringAndMaps() {
        UUID id = UUID.randomUUID();
        var entity = new ProductTypeEntity();
        entity.setId(id);

        var model = ProductTypeModel.builder().id(id).build();

        when(jpaRepo.findById(id.toString())).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        assertThat(adapter.findById(id)).contains(model);
    }
}