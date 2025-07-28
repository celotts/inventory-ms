package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.usecase.ProductUnitUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductUnitServiceTest {

    @Mock
    private ProductUnitUseCase useCase;

    @Mock
    private ProductUnitRepositoryPort repository;

    @Mock
    private ProductUnitDtoMapper mapper;

    private ProductUnitService service;

    private UUID id;
    private ProductUnitCreateDto createDto;
    private ProductUnitUpdateDto updateDto;
    private ProductUnitModel model;
    private ProductUnitResponseDto response;

    @BeforeEach
    void setUp() {
        service = new ProductUnitService(useCase, repository, mapper);

        id = UUID.randomUUID();

        createDto = ProductUnitCreateDto.builder()
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .enabled(true)
                .createdBy("admin")
                .build();

        updateDto = ProductUnitUpdateDto.builder()
                .name("Kg actualizado")
                .description("Peso actualizado")
                .enabled(false)
                .updatedBy("admin")
                .build();

        model = ProductUnitModel.builder()
                .id(id)
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .build();

        response = ProductUnitResponseDto.builder()
                .id(id)
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .build();
    }

    @Test
    void create_shouldSaveAndReturnResponse() {
        when(repository.existsByCode("KG")).thenReturn(false);
        when(mapper.toModel(createDto)).thenReturn(model);
        when(useCase.save(any())).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(response);

        ProductUnitResponseDto result = service.create(createDto);

        assertNotNull(result);
        assertEquals("KG", result.getCode());
        verify(useCase).save(any());
    }

    @Test
    void create_shouldThrowException_whenCodeExists() {
        when(repository.existsByCode("KG")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(createDto));
    }

    @Test
    void findAll_shouldReturnListOfResponses() {
        when(useCase.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(response);

        List<ProductUnitResponseDto> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("KG", result.get(0).getCode());
    }

    @Test
    void findById_shouldReturnResponse() {
        when(useCase.findById(id)).thenReturn(Optional.of(model));
        when(mapper.toResponse(model)).thenReturn(response);

        ProductUnitResponseDto result = service.findById(id);

        assertEquals(id, result.getId());
        assertEquals("KG", result.getCode());
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(useCase.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(id));
    }

    @Test
    void update_shouldModifyAndReturnResponse() {
        when(useCase.findById(id)).thenReturn(Optional.of(model));
        when(useCase.save(any())).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(response);

        ProductUnitResponseDto result = service.update(id, updateDto);

        assertEquals("KG", result.getCode());
        verify(useCase).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(useCase.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(id, updateDto));
    }

    @Test
    void delete_shouldCallDelete_whenExists() {
        when(useCase.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(useCase).deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        when(useCase.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(id));
    }

    @Test
    void existsByCode_shouldReturnTrue() {
        when(useCase.existsByCode("KG")).thenReturn(true);

        assertTrue(service.existsByCode("KG"));
    }

    @Test
    void findNameByCode_shouldReturnName() {
        when(useCase.findNameByCode("KG")).thenReturn(Optional.of("Kilogramo"));

        Optional<String> result = service.findNameByCode("KG");

        assertTrue(result.isPresent());
        assertEquals("Kilogramo", result.get());
    }

    @Test
    void findAllCodes_shouldReturnCodes() {
        when(useCase.findAllCodes()).thenReturn(List.of("KG", "G"));

        List<String> result = service.findAllCodes();

        assertEquals(2, result.size());
    }
}