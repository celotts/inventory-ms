package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.CategoryUseCaseImpl;
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Esta clase reemplaza el antiguo CategoryServiceTest.
 * Ahora probamos directamente el caso de uso (puerto/aplicación): CategoryUseCaseImpl.
 *
 * 👉 El servicio "CategoryService" fue eliminado. La lógica vive en CategoryUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock private CategoryRepositoryPort repository;

    private CategoryUseCaseImpl useCase;

    private UUID id;
    private CategoryModel category;

    @BeforeEach
    void setUp() {
        useCase = new CategoryUseCaseImpl(repository);

        id = UUID.randomUUID();
        category = CategoryModel.builder()
                .id(id)
                .name("Tacos")
                .description("Comida mexicana")
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy("tester")
                .build();
    }

    @Test
    void save_ShouldDelegateToRepository() {
        // Probar la rama de "create": id == null
        CategoryModel newCategory = CategoryModel.builder()
                .id(null)
                .name("Tacos")
                .description("Comida mexicana")
                .active(true)
                .build();

        when(repository.existsByName("Tacos")).thenReturn(false);
        when(repository.save(any(CategoryModel.class))).thenReturn(newCategory);

        CategoryModel result = useCase.save(newCategory);

        assertNotNull(result);
        verify(repository).save(any(CategoryModel.class));
    }

    @Test
    void findById_ShouldReturnCategory() {
        when(repository.findById(id)).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Tacos", result.get().getName());
    }

    @Test
    void deleteById_ShouldValidateExistence() {
        when(repository.existsById(id)).thenReturn(true);

        useCase.deleteById(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowWhenNotExists() {
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.deleteById(id));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void findAllPaginated_ShouldDelegateToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(category));
        when(repository.findAllPaginated("Tac", true, pageable)).thenReturn(page);

        Page<CategoryModel> result = useCase.findAllPaginated("Tac", true, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateStatus_ShouldUpdateActiveAndAudit() {
        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);

        CategoryModel updated = useCase.updateStatus(id, false);

        assertFalse(updated.getActive());
        verify(repository).save(category);
    }

    @Test
    void getCategoryStatistics_ShouldAggregateCounts() {
        when(repository.findAll()).thenReturn(List.of(category));
        when(repository.countByActive(true)).thenReturn(1L);
        when(repository.countByActive(false)).thenReturn(0L);

        CategoryStatusDto stats = useCase.getCategoryStatistics();

        assertEquals(1, stats.getTotalCategories());
        assertEquals(1, stats.getActiveCategories());
        assertEquals(0, stats.getInactiveCategories());
    }
}