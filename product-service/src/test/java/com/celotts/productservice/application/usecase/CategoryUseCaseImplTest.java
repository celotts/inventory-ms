package com.celotts.productservice.application.usecase.category;

import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.model.category.CategoryStats;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseImplTest {

    @Mock
    private CategoryRepositoryPort repository;

    @InjectMocks
    private CategoryUseCaseImpl useCase;

    private UUID id;
    private CategoryModel model;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        model = CategoryModel.builder()
                .id(id)
                .name("Bebidas")
                .description("Todas las bebidas")
                .active(true)
                .createdBy("tester")
                .updatedBy("tester")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .deleted(false)
                .build();
    }

    @Test
    void save_delegatesToRepository() {
        when(repository.save(model)).thenReturn(model);

        var result = useCase.save(model);

        assertSame(model, result);
        verify(repository).save(model);
    }

    @Test
    void findById_returnsOptional() {
        when(repository.findById(id)).thenReturn(Optional.of(model));

        var found = useCase.findById(id);

        assertTrue(found.isPresent());
        assertEquals(model, found.get());
        verify(repository).findById(id);
    }

    @Test
    void findByName_returnsOptional() {
        when(repository.findByName("Bebidas")).thenReturn(Optional.of(model));

        var found = useCase.findByName("Bebidas");

        assertTrue(found.isPresent());
        assertEquals(model, found.get());
        verify(repository).findByName("Bebidas");
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(model));

        var list = useCase.findAll();

        assertEquals(1, list.size());
        assertEquals(model, list.get(0));
        verify(repository).findAll();
    }

    @Test
    void findAllById_returnsList() {
        var ids = List.of(id, UUID.randomUUID());
        when(repository.findAllById(ids)).thenReturn(List.of(model));

        var list = useCase.findAllById(ids);

        assertEquals(1, list.size());
        verify(repository).findAllById(ids);
    }

    @Test
    void findByNameContaining_returnsList() {
        when(repository.findByNameContaining("beb")).thenReturn(List.of(model));

        var list = useCase.findByNameContaining("beb");

        assertEquals(1, list.size());
        verify(repository).findByNameContaining("beb");
    }

    @Test
    void searchByNameOrDescription_returnsList() {
        when(repository.findByNameOrDescription("beb", 5)).thenReturn(List.of(model));

        var list = useCase.searchByNameOrDescription("beb", 5);

        assertEquals(1, list.size());
        verify(repository).findByNameOrDescription("beb", 5);
    }

    @Test
    void deleteById_callsRepository() {
        doNothing().when(repository).deleteById(id);

        useCase.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void existsById_and_existsByName_delegate() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.existsByName("Bebidas")).thenReturn(false);

        assertTrue(useCase.existsById(id));
        assertFalse(useCase.existsByName("Bebidas"));

        verify(repository).existsById(id);
        verify(repository).existsByName("Bebidas");
    }

    @Test
    void pageable_findAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<CategoryModel> page = new PageImpl<>(List.of(model), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        var result = useCase.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    void pageable_findByNameContaining() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(model), pageable, 1);
        when(repository.findByNameContaining("beb", pageable)).thenReturn(page);

        var result = useCase.findByNameContaining("beb", pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findByNameContaining("beb", pageable);
    }

    @Test
    void pageable_findByActive() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(model), pageable, 1);
        when(repository.findByActive(true, pageable)).thenReturn(page);

        var result = useCase.findByActive(true, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findByActive(true, pageable);
    }

    @Test
    void pageable_findByNameContainingAndActive() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(model), pageable, 1);
        when(repository.findByNameContainingAndActive("beb", true, pageable)).thenReturn(page);

        var result = useCase.findByNameContainingAndActive("beb", true, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findByNameContainingAndActive("beb", true, pageable);
    }

    @Test
    void pageable_findAllPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryModel> page = new PageImpl<>(List.of(model), pageable, 1);
        when(repository.findAllPaginated("beb", true, pageable)).thenReturn(page);

        var result = useCase.findAllPaginated("beb", true, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findAllPaginated("beb", true, pageable);
    }

    @Test
    void updateStatus_updatesActive_andSaves() {
        when(repository.findById(id)).thenReturn(Optional.of(model));
        when(repository.save(any(CategoryModel.class))).thenAnswer(inv -> inv.getArgument(0));

        var beforeUpdatedAt = model.getUpdatedAt();

        var updated = useCase.updateStatus(id, false);

        assertFalse(updated.getActive());
        assertNotNull(updated.getUpdatedAt());
        assertTrue(updated.getUpdatedAt().isAfter(beforeUpdatedAt));

        ArgumentCaptor<CategoryModel> captor = ArgumentCaptor.forClass(CategoryModel.class);
        verify(repository).save(captor.capture());
        assertFalse(captor.getValue().getActive());
        verify(repository).findById(id);
    }

    @Test
    void updateStatus_throws_whenNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () -> useCase.updateStatus(id, true));
        assertEquals("Category not found", ex.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void restore_setsDeletedFalse_andSaves() {
        // arrancamos con deleted=true para validar el cambio
        var deletedModel = model.toBuilder().deleted(true).build();
        when(repository.findById(id)).thenReturn(Optional.of(deletedModel));
        when(repository.save(any(CategoryModel.class))).thenAnswer(inv -> inv.getArgument(0));

        var restored = useCase.restore(id);

        assertFalse(restored.isDeleted());
        assertNotNull(restored.getUpdatedAt());
        verify(repository).findById(id);
        verify(repository).save(any(CategoryModel.class));
    }

    @Test
    void restore_throws_whenNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class, () -> useCase.restore(id));
        assertEquals("Category not found", ex.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void permanentDelete_whenExists_callsDelete() {
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        useCase.permanentDelete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void permanentDelete_whenNotExists_throws() {
        when(repository.existsById(id)).thenReturn(false);

        var ex = assertThrows(IllegalArgumentException.class, () -> useCase.permanentDelete(id));
        assertEquals("Category not found", ex.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void getCategoryStatistics_aggregatesCounts() {
        when(repository.count()).thenReturn(10L);
        when(repository.countByActive(true)).thenReturn(7L);
        when(repository.countByActive(false)).thenReturn(3L);

        CategoryStats stats = useCase.getCategoryStatistics();

        assertEquals(10L, stats.getTotalCategories());
        assertEquals(7L, stats.getActiveCategories());
        assertEquals(3L, stats.getInactiveCategories());

        verify(repository).count();
        verify(repository).countByActive(true);
        verify(repository).countByActive(false);
    }

    @Test
    void countByActive_delegates() {
        when(repository.countByActive(true)).thenReturn(5L);

        assertEquals(5L, useCase.countByActive(true));
        verify(repository).countByActive(true);
    }
}