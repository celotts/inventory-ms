package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryUseCaseImplTest {

    private CategoryRepositoryPort repository;
    private CategoryUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CategoryRepositoryPort.class);
        useCase = new CategoryUseCaseImpl(repository);
    }

    @Test
    void save_ShouldSaveCategory() {
        CategoryModel category = mock(CategoryModel.class);
        when(repository.save(category)).thenReturn(category);

        CategoryModel result = useCase.save(category);

        assertNotNull(result);
        verify(repository).save(category);
    }

    @Test
    void findById_ShouldReturnCategory() {
        UUID id = UUID.randomUUID();
        CategoryModel category = mock(CategoryModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void findByName_ShouldReturnCategory() {
        String name = "Bebidas";
        CategoryModel category = mock(CategoryModel.class);
        when(repository.findByName(name)).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = useCase.findByName(name);

        assertTrue(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAll() {
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findAll()).thenReturn(list);

        List<CategoryModel> result = useCase.findAll();

        assertEquals(list, result);
    }

    @Test
    void findByNameContaining_ShouldReturnMatches() {
        String name = "be";
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findByNameContaining(name)).thenReturn(list);

        List<CategoryModel> result = useCase.findByNameContaining(name);

        assertEquals(list, result);
    }

    @Test
    void existsByName_ShouldReturnTrue() {
        when(repository.existsByName("Carnes")).thenReturn(true);
        assertTrue(useCase.existsByName("Carnes"));
    }

    @Test
    void deleteById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();
        useCase.deleteById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void existsById_ShouldReturnFalse() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);
        assertFalse(useCase.existsById(id));
    }

    @Test
    void findByActive_ShouldReturnList() {
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findByActive(true)).thenReturn(list);
        assertEquals(list, useCase.findByActive(true));
    }

    @Test
    void findAllPageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)));
        when(repository.findAll(pageable)).thenReturn(page);

        assertEquals(page, useCase.findAll(pageable));
    }

    @Test
    void findByNameContainingPageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)));
        when(repository.findByNameContaining("be", pageable)).thenReturn(page);

        assertEquals(page, useCase.findByNameContaining("be", pageable));
    }

    @Test
    void findByActivePageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)));
        when(repository.findByActive(true, pageable)).thenReturn(page);

        assertEquals(page, useCase.findByActive(true, pageable));
    }

    @Test
    void findByNameAndActivePageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)));
        when(repository.findByNameContainingAndActive("be", true, pageable)).thenReturn(page);

        assertEquals(page, useCase.findByNameContainingAndActive("be", true, pageable));
    }

    @Test
    void searchByNameOrDescription_ShouldReturnList() {
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findByNameOrDescription("bebida", 5)).thenReturn(list);

        assertEquals(list, useCase.searchByNameOrDescription("bebida", 5));
    }

    @Test
    void findAllById_ShouldReturnList() {
        List<UUID> ids = List.of(UUID.randomUUID());
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findAllById(ids)).thenReturn(list);

        assertEquals(list, useCase.findAllById(ids));
    }

    @Test
    void findAllPaginated_ShouldDelegateToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)));
        when(repository.findAllPaginated("bebida", true, pageable)).thenReturn(page);

        assertEquals(page, useCase.findAllPaginated("bebida", true, pageable));
    }

    @Test
    void updateStatus_ShouldUpdateActiveField() {
        UUID id = UUID.randomUUID();
        CategoryModel model = mock(CategoryModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(model));
        when(repository.save(model)).thenReturn(model);

        CategoryModel result = useCase.updateStatus(id, false);

        assertNotNull(result);
        verify(model).setActive(false);
        verify(model).setUpdatedAt(any(LocalDateTime.class));
        verify(repository).save(model);
    }

    @Test
    void updateStatus_ShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.updateStatus(id, true));
    }

    @Test
    void restore_ShouldSetDeletedFalse() {
        UUID id = UUID.randomUUID();
        CategoryModel model = mock(CategoryModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(model));
        when(repository.save(model)).thenReturn(model);

        CategoryModel result = useCase.restore(id);

        verify(model).setDeleted(false);
        verify(model).setUpdatedAt(any(LocalDateTime.class));
        verify(repository).save(model);
        assertEquals(model, result);
    }

    @Test
    void restore_ShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.restore(id));
    }

    @Test
    void permanentDelete_ShouldThrowIfNotExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.permanentDelete(id));
    }

    @Test
    void permanentDelete_ShouldDeleteIfExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        useCase.permanentDelete(id);
        verify(repository).deleteById(id);
    }

    @Test
    void getCategoryStatistics_ShouldReturnCorrectCounts() {
        List<CategoryModel> all = List.of(mock(CategoryModel.class), mock(CategoryModel.class));
        when(repository.findAll()).thenReturn(all);
        when(repository.countByActive(true)).thenReturn(1L);
        when(repository.countByActive(false)).thenReturn(1L);

        CategoryStatsDto stats = useCase.getCategoryStatistics();

        assertEquals(2, stats.getTotalCategories());
        assertEquals(1, stats.getActiveCategories());
        assertEquals(1, stats.getInactiveCategories());
    }

    @Test
    void countByActive_ShouldReturnCorrectValue() {
        when(repository.countByActive(true)).thenReturn(3L);
        assertEquals(3L, useCase.countByActive(true));
    }

    @Test
    void findByNameOrDescription_ShouldReturnList() {
        String query = "bebida";
        int limit = 5;
        List<CategoryModel> list = List.of(mock(CategoryModel.class));

        when(repository.findByNameOrDescription(query, limit)).thenReturn(list);

        List<CategoryModel> result = useCase.findByNameOrDescription(query, limit);

        assertEquals(list, result);
        verify(repository).findByNameOrDescription(query, limit);
    }
}