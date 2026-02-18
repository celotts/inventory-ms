package com.celotts.productservice.application.usecase.category;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.category.CategoryModel;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseImplTest {

    @Mock
    private CategoryRepositoryPort repository;

    @InjectMocks
    private CategoryUseCaseImpl useCase;

    @Test
    void updateStatus_throws_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.updateStatus(id, true));
    }

    @Test
    void restore_throws_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.restore(id));
    }

    @Test
    void restore_setsDeletedFalse_andSaves() {
        UUID id = UUID.randomUUID();
        CategoryModel existing = CategoryModel.builder()
                .id(id)
                .active(false)
                .deleted(true)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(CategoryModel.class))).thenAnswer(i -> i.getArguments()[0]);

        CategoryModel result = useCase.restore(id);

        assertTrue(result.getActive());
        assertFalse(result.isDeleted());
        verify(repository).save(existing);
    }

    @Test
    void permanentDelete_whenNotExists_throws() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> useCase.permanentDelete(id));
    }

    @Test
    void permanentDelete_whenExists_deletes() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        useCase.permanentDelete(id);

        verify(repository).deleteById(id);
    }
}