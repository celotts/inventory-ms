package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductTypeUseCaseImplTest {

    private ProductTypeRepository productTypeRepository;
    private ProductTypeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        productTypeRepository = mock(ProductTypeRepository.class);
        useCase = new ProductTypeUseCaseImpl(productTypeRepository);
    }

    @Test
    void shouldReturnTrue_WhenCodeExists() {
        when(productTypeRepository.existsByCode("FOOD")).thenReturn(true);

        boolean exists = useCase.existsByCode("FOOD");

        assertTrue(exists);
        verify(productTypeRepository).existsByCode("FOOD");
    }

    @Test
    void shouldReturnFalse_WhenCodeDoesNotExist() {
        when(productTypeRepository.existsByCode("INVALID")).thenReturn(false);

        boolean exists = useCase.existsByCode("INVALID");

        assertFalse(exists);
        verify(productTypeRepository).existsByCode("INVALID");
    }

    @Test
    void shouldReturnName_WhenCodeIsFound() {
        when(productTypeRepository.findNameByCode("FOOD")).thenReturn(Optional.of("Food"));

        Optional<String> name = useCase.findNameByCode("FOOD");

        assertTrue(name.isPresent());
        assertEquals("Food", name.get());
    }

    @Test
    void shouldReturnEmpty_WhenCodeIsNotFound() {
        when(productTypeRepository.findNameByCode("UNKNOWN")).thenReturn(Optional.empty());

        Optional<String> name = useCase.findNameByCode("UNKNOWN");

        assertTrue(name.isEmpty());
    }

    @Test
    void shouldReturnAllCodes() {
        List<String> codes = List.of("FOOD", "BEV", "TOOLS");
        when(productTypeRepository.findAllCodes()).thenReturn(codes);

        List<String> result = useCase.findAllCodes();

        assertEquals(codes.size(), result.size());
        assertEquals(codes, result);
    }

    @Test
    void shouldReturnProductTypeEntity_WhenCodeExists() {
        ProductTypeEntity entity = new ProductTypeEntity();
        when(productTypeRepository.findByCode("FOOD")).thenReturn(Optional.of(entity));

        Optional<ProductTypeEntity> result = useCase.findByCode("FOOD");

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void shouldReturnEmpty_WhenProductTypeCodeNotFound() {
        when(productTypeRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        Optional<ProductTypeEntity> result = useCase.findByCode("UNKNOWN");

        assertFalse(result.isPresent());
    }
}