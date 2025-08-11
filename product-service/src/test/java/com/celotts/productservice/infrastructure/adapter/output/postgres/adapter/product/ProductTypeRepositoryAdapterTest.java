package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductTypeRepositoryAdapterTest {

    private ProductTypeRepository repository;
    private ProductTypeRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(ProductTypeRepository.class);
        adapter = new ProductTypeRepositoryAdapter(repository);
    }

    @Test
    void testExistsByCode() {
        when(repository.existsByCode("TYPE001")).thenReturn(true);
        boolean result = adapter.existsByCode("TYPE001");
        assertThat(result).isTrue();
    }

    @Test
    void testFindNameByCode() {
        when(repository.findNameByCode("TYPE001")).thenReturn(Optional.of("Electrónica"));
        var result = adapter.findNameByCode("TYPE001");
        assertThat(result).isPresent().contains("Electrónica");
    }

    @Test
    void testFindAllCodes() {
        List<String> codes = List.of("TYPE001", "TYPE002");
        when(repository.findAllCodes()).thenReturn(codes);
        var result = adapter.findAllCodes();
        assertThat(result).containsExactly("TYPE001", "TYPE002");
    }

    @Test
    void testFindByCode() {
        ProductTypeEntity entity = new ProductTypeEntity();
        entity.setCode("TYPE001");
        entity.setName("Electrónica");
        when(repository.findByCode("TYPE001")).thenReturn(Optional.of(entity));
        var result = adapter.findByCode("TYPE001");
        assertThat(result).isPresent().contains(entity);
    }
}