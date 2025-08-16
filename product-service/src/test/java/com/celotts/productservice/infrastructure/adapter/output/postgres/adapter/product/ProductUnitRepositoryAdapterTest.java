package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product.ProductUnitRepositoryAdapter;
import com.celotts.productserviceOinfrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductUnitEntityMapper;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product.ProductUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductUnitRepositoryAdapterTest {

    private ProductUnitRepository jpaRepo;
    private ProductUnitEntityMapper mapper;
    private ProductUnitRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepo = mock(ProductUnitRepository.class);
        mapper = mock(ProductUnitEntityMapper.class);
        adapter = new ProductUnitRepositoryAdapter(jpaRepo, mapper);
    }

    // ==== Light queries ====

    @Test
    void testExistsByCode() {
        when(jpaRepo.existsByCode("CODE1")).thenReturn(true);
        boolean result = adapter.existsByCode("CODE1");
        assertThat(result).isTrue();
    }

    @Test
    void testFindNameByCode() {
        when(jpaRepo.findNameByCode("CODE1")).thenReturn(Optional.of("Unidad"));
        var result = adapter.findNameByCode("CODE1");
        assertThat(result).isPresent().contains("Unidad");
    }

    @Test
    void testFindAllCodes() {
        List<String> codes = List.of("CODE1", "CODE2");
        when(jpaRepo.findAllCodes()).thenReturn(codes);
        var result = adapter.findAllCodes();
        assertThat(result).containsExactly("CODE1", "CODE2");
    }

    // ==== CRUD ====

    @Test
    void testSave() {
        ProductUnitModel model = createModel();
        ProductUnitEntity entity = createEntity();
        ProductUnitEntity saved = createEntity();
        saved.setId(model.getId());

        when(mapper.toEntity(model)).thenReturn(entity);
        when(jpaRepo.save(entity)).thenReturn(saved);
        when(mapper.toModel(saved)).thenReturn(model);

        ProductUnitModel result = adapter.save(model);

        assertThat(result).isEqualTo(model);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        ProductUnitEntity entity = createEntity();
        ProductUnitModel model = createModel();
        when(jpaRepo.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        var result = adapter.findById(id);

        assertThat(result).isPresent().contains(model);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpaRepo).deleteById(id);
    }

    @Test
    void testFindAll() {
        List<ProductUnitEntity> entities = List.of(createEntity());
        List<ProductUnitModel> models = List.of(createModel());

        when(jpaRepo.findAll()).thenReturn(entities);
        when(mapper.toModel(entities.get(0))).thenReturn(models.get(0));

        var result = adapter.findAll();

        assertThat(result).isEqualTo(models);
    }

    // ==== Helpers ====

    private ProductUnitModel createModel() {
        return ProductUnitModel.builder()
                .id(UUID.randomUUID())
                .code("CODE1")
                .name("Unidad")
                .symbol("u")
                .enabled(true)  // <- aquí
                .build();
    }

    private ProductUnitEntity createEntity() {
        ProductUnitEntity entity = new ProductUnitEntity();
        entity.setId(UUID.randomUUID());
        entity.setCode("CODE1");
        entity.setName("Unidad");
        entity.setSymbol("u");
        entity.setEnabled(true);
        return entity;
    }
}