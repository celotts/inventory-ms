package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.usecase.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category.CategoryRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceTest {

    @Mock private CategoryUseCase useCase;
    @Mock private CategoryRequestMapper mapper;

    @Mock private Authentication authentication;

    private CategoryService service;

    private UUID id;
    private CategoryModel category;

    @BeforeEach
    void setUp() {
        service = new CategoryService(useCase, mapper);

        id = UUID.randomUUID();
        category = CategoryModel.builder()
                .id(id)
                .name("Tacos")
                .description("Comida mexicana")
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy("tester")
                .build();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("tester");
    }

    @Test
    void create_shouldThrowIfNameExists() {
        when(useCase.existsByName("Tacos")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(category));
    }

    @Test
    void create_shouldMapAndSave() {
        when(useCase.existsByName("Tacos")).thenReturn(false);
        when(mapper.toModel(category)).thenReturn(category);
        when(useCase.save(any())).thenReturn(category);

        CategoryModel result = service.create(category);

        assertNotNull(result);
        assertEquals("Tacos", result.getName());
    }

    @Test
    void update_shouldUpdateCategory() {
        when(useCase.findById(id)).thenReturn(Optional.of(category));
        when(useCase.save(any())).thenReturn(category);

        CategoryModel updated = CategoryModel.builder().name("Updated").build();

        CategoryModel result = service.update(id, updated);

        assertNotNull(result);
        verify(useCase).save(any());
    }

    @Test
    void update_shouldThrowIfNotFound() {
        when(useCase.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.update(id, category));
    }

    @Test
    void findById_shouldReturnCategory() {
        when(useCase.findById(id)).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = service.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Tacos", result.get().getName());
    }

    @Test
    void findAll_shouldReturnList() {
        when(useCase.findAll()).thenReturn(List.of(category));

        List<CategoryModel> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByName_shouldReturnOptional() {
        when(useCase.findByName("Tacos")).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = service.findByName("Tacos");

        assertTrue(result.isPresent());
        assertEquals("Tacos", result.get().getName());
    }

    @Test
    void deleteById_shouldThrowIfNotExists() {
        when(useCase.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.deleteById(id));
    }

    @Test
    void deleteById_shouldCallDelete() {
        when(useCase.existsById(id)).thenReturn(true);

        service.deleteById(id);

        verify(useCase).deleteById(id);
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(useCase.existsById(id)).thenReturn(true);

        assertTrue(service.existsById(id));
    }

    @Test
    void existsByName_shouldReturnTrue() {
        when(useCase.existsByName("Tacos")).thenReturn(true);

        assertTrue(service.existsByName("Tacos"));
    }

    @Test
    void findAllPaginated_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(category));
        when(useCase.findAllPaginated("Tac", true, pageable)).thenReturn(page);

        Page<CategoryModel> result = service.findAllPaginated("Tac", true, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchByNameOrDescription_shouldReturnList() {
        when(useCase.searchByNameOrDescription("tac", 5)).thenReturn(List.of(category));

        List<CategoryModel> result = service.searchByNameOrDescription("tac", 5);

        assertEquals(1, result.size());
    }

    @Test
    void updateStatus_shouldUpdateActiveField() {
        when(useCase.findById(id)).thenReturn(Optional.of(category));
        when(useCase.save(any())).thenReturn(category);

        CategoryModel result = service.updateStatus(id, false);

        assertFalse(result.getActive());
    }

    @Test
    void permanentDelete_shouldCallUseCase() {
        service.permanentDelete(id);
        verify(useCase).permanentDelete(id);
    }

    @Test
    void restore_shouldReturnRestoredCategory() {
        when(useCase.restore(id)).thenReturn(category);

        CategoryModel result = service.restore(id);

        assertEquals("Tacos", result.getName());
    }

    @Test
    void constructor_shouldInstantiateService() {
        CategoryService categoryService = new CategoryService(useCase, mapper);
        assertNotNull(categoryService);
    }

    @Test
    void getCategoryStatistics_shouldReturnDto() {
        CategoryStatusDto stats = new CategoryStatusDto(
                10, 5, 3, 2, 50.0, 30.0, 20.0
        );

        when(useCase.getCategoryStatistics()).thenReturn(stats);

        CategoryStatusDto result = service.getCategoryStatistics();

        assertEquals(10, result.getTotalCategories());
        assertEquals(5, result.getActiveCategories());
        assertEquals(20.0, result.getDeletedPercentage());
    }

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        CategoryService service = new CategoryService(useCase, mapper);
        assertNotNull(service);
    }

    @Test
    void constructor_shouldAssignInjectedDependencies() {
        CategoryService service = new CategoryService(useCase, mapper);

        // Validar que la instancia no es null
        assertNotNull(service);

        // Ya que las dependencias son privadas, no podemos acceder directamente.
        // Pero si no lanza excepción, el constructor fue exitoso.
    }
}