package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.product.ProductUnitUseCaseImpl;
import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUnitUseCaseImplTest {

    @Mock
    private ProductUnitRepositoryPort repository;

    private ProductUnitUseCaseImpl useCase;

    private UUID id;
    private ProductUnitModel model;

    @BeforeEach
    void setUp() {
        // El UseCaseImpl sólo recibe el repository (1 arg)
        useCase = new ProductUnitUseCaseImpl(repository);

        id = UUID.randomUUID();
        model = ProductUnitModel.builder()
                .id(id)
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .symbol("kg")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .build();
    }

    @Test
    void save_shouldPersistAndReturnModel() {
        when(repository.save(model)).thenReturn(model);

        var result = useCase.save(model);

        assertNotNull(result);
        assertEquals("KG", result.getCode());
        verify(repository).save(model);
    }

    @Test
    void findAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(model));

        var result = useCase.findAll();

        assertEquals(1, result.size());
        assertEquals("KG", result.get(0).getCode());
        verify(repository).findAll();
    }

    @Test
    void findById_found() {
        when(repository.findById(id)).thenReturn(Optional.of(model));

        var result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(repository).findById(id);
    }

    @Test
    void findById_notFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = useCase.findById(id);

        assertTrue(result.isEmpty());
        verify(repository).findById(id);
    }

    @Test
    void deleteById_whenExists() {
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        useCase.deleteById(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteById_whenNotExists_shouldThrow() {
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.deleteById(id));
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void existsByCode_true() {
        when(repository.existsByCode("KG")).thenReturn(true);

        assertTrue(useCase.existsByCode("KG"));
        verify(repository).existsByCode("KG");
    }

    @Test
    void findNameByCode_shouldReturnName() {
        when(repository.findNameByCode("KG")).thenReturn(Optional.of("Kilogramo"));

        var result = useCase.findNameByCode("KG");

        assertTrue(result.isPresent());
        assertEquals("Kilogramo", result.get());
        verify(repository).findNameByCode("KG");
    }

    @Test
    void findAllCodes_shouldReturnCodes() {
        when(repository.findAllCodes()).thenReturn(List.of("KG", "G"));

        var result = useCase.findAllCodes();

        assertEquals(List.of("KG", "G"), result);
        verify(repository).findAllCodes();
    }
}