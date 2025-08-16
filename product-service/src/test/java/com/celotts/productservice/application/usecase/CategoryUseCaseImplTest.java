package com.celotts.productservice.application.usecase;

import com.celotts.productservice.application.usecase.category.CategoryUseCaseImpl;
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.model.CategoryStats;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
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

    // ----------------- CRUD básico -----------------

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
        verify(repository).findById(id);
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<CategoryModel> result = useCase.findById(id);

        assertTrue(result.isEmpty());
        verify(repository).findById(id);
    }

    @Test
    void findByName_ShouldReturnCategory() {
        String name = "Bebidas";
        CategoryModel category = mock(CategoryModel.class);
        when(repository.findByName(name)).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = useCase.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
        verify(repository).findByName(name);
    }

    @Test
    void findByName_ShouldReturnEmptyWhenNotFound() {
        when(repository.findByName("NoExiste")).thenReturn(Optional.empty());

        Optional<CategoryModel> result = useCase.findByName("NoExiste");

        assertTrue(result.isEmpty());
        verify(repository).findByName("NoExiste");
    }

    @Test
    void findAll_ShouldReturnAll() {
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findAll()).thenReturn(list);

        List<CategoryModel> result = useCase.findAll();

        assertEquals(list, result);
        verify(repository).findAll();
    }

    @Test
    void findAllById_ShouldReturnList() {
        List<UUID> ids = List.of(UUID.randomUUID());
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findAllById(ids)).thenReturn(list);

        assertEquals(list, useCase.findAllById(ids));
        verify(repository).findAllById(ids);
    }

    @Test
    void findAllById_ShouldReturnEmptyWhenNone() {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(repository.findAllById(ids)).thenReturn(List.of());

        List<CategoryModel> result = useCase.findAllById(ids);

        assertTrue(result.isEmpty());
        verify(repository).findAllById(ids);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();

        useCase.deleteById(id);

        verify(repository).deleteById(id);
    }

    // ----------------- Exists -----------------

    @Test
    void existsByName_ShouldReturnTrue() {
        when(repository.existsByName("Carnes")).thenReturn(true);
        assertTrue(useCase.existsByName("Carnes"));
        verify(repository).existsByName("Carnes");
    }

    @Test
    void existsByName_ShouldReturnFalse() {
        when(repository.existsByName("Verduras")).thenReturn(false);
        assertFalse(useCase.existsByName("Verduras"));
        verify(repository).existsByName("Verduras");
    }

    @Test
    void existsById_ShouldReturnFalse() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);
        assertFalse(useCase.existsById(id));
        verify(repository).existsById(id);
    }

    @Test
    void existsById_ShouldReturnTrue() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        assertTrue(useCase.existsById(id));
        verify(repository).existsById(id);
    }

    // ----------------- Búsquedas & paginación -----------------

    @Test
    void findByNameContaining_ShouldReturnMatches() {
        String name = "be";
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findByNameContaining(name)).thenReturn(list);

        List<CategoryModel> result = useCase.findByNameContaining(name);

        assertEquals(list, result);
        verify(repository).findByNameContaining(name);
    }

    @Test
    void searchByNameOrDescription_ShouldReturnList() {
        List<CategoryModel> list = List.of(mock(CategoryModel.class));
        when(repository.findByNameOrDescription("bebida", 5)).thenReturn(list);

        assertEquals(list, useCase.searchByNameOrDescription("bebida", 5));
        verify(repository).findByNameOrDescription("bebida", 5);
    }

    @Test
    void findAllPageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        assertEquals(page, useCase.findAll(pageable));
        verify(repository).findAll(pageable);
    }

    @Test
    void findByNameContainingPageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)), pageable, 1);
        when(repository.findByNameContaining("be", pageable)).thenReturn(page);

        assertEquals(page, useCase.findByNameContaining("be", pageable));
        verify(repository).findByNameContaining("be", pageable);
    }

    @Test
    void findByActivePageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)), pageable, 1);
        when(repository.findByActive(true, pageable)).thenReturn(page);

        assertEquals(page, useCase.findByActive(true, pageable));
        verify(repository).findByActive(true, pageable);
    }

    @Test
    void findByNameAndActivePageable_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)), pageable, 1);
        when(repository.findByNameContainingAndActive("be", true, pageable)).thenReturn(page);

        assertEquals(page, useCase.findByNameContainingAndActive("be", true, pageable));
        verify(repository).findByNameContainingAndActive("be", true, pageable);
    }

    @Test
    void findAllPaginated_ShouldDelegateToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(mock(CategoryModel.class)), pageable, 1);
        when(repository.findAllPaginated("bebida", true, pageable)).thenReturn(page);

        assertEquals(page, useCase.findAllPaginated("bebida", true, pageable));
        verify(repository).findAllPaginated("bebida", true, pageable);
    }

    @Test
    void findAllPaginated_ShouldAllowNullFilters() {
        Pageable pageable = PageRequest.of(1, 5);
        Page<CategoryModel> page = new PageImpl<>(List.of(), pageable, 0);
        when(repository.findAllPaginated(null, null, pageable)).thenReturn(page);

        Page<CategoryModel> result = useCase.findAllPaginated(null, null, pageable);

        assertSame(page, result);
        verify(repository).findAllPaginated(null, null, pageable);
    }

    // ----------------- Mutaciones con validación -----------------

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
        verify(repository).findById(id);
        verify(repository, never()).save(any());
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
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void permanentDelete_ShouldThrowIfNotExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.permanentDelete(id));
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void permanentDelete_ShouldDeleteIfExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        useCase.permanentDelete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    // ----------------- Estadísticas & conteo -----------------

    @Test
    void getCategoryStatistics_ShouldReturnCorrectCounts() {
        when(repository.count()).thenReturn(10L);
        when(repository.countByActive(true)).thenReturn(7L);
        when(repository.countByActive(false)).thenReturn(3L);

        CategoryStats stats = useCase.getCategoryStatistics();

        assertNotNull(stats);
        assertEquals(10L, stats.getTotalCategories());
        assertEquals(7L, stats.getActiveCategories());
        assertEquals(3L, stats.getInactiveCategories());

        verify(repository).count();
        verify(repository).countByActive(true);
        verify(repository).countByActive(false);
    }

    @Test
    void countByActive_ShouldReturnCorrectValue() {
        when(repository.countByActive(true)).thenReturn(3L);
        assertEquals(3L, useCase.countByActive(true));
        verify(repository).countByActive(true);
    }
}