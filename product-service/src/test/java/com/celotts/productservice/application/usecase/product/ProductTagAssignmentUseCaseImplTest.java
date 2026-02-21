package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.domain.port.output.product.ProductTagAssignmentRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTagAssignmentUseCaseImplTest {

    @Mock
    private ProductTagAssignmentRepositoryPort repository;

    @InjectMocks
    private ProductTagAssignmentUseCaseImpl useCase;

    @Test
    void findById_WhenExists_ShouldReturnModel() {
        UUID id = UUID.randomUUID();
        ProductTagAssignmentModel expected = new ProductTagAssignmentModel();
        when(repository.findById(id)).thenReturn(Optional.of(expected));

        ProductTagAssignmentModel result = useCase.findById(id);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(id));
    }

    @Test
    void enable_ShouldDelegateToRepository() {
        UUID id = UUID.randomUUID();
        ProductTagAssignmentModel expected = new ProductTagAssignmentModel();
        expected.setEnabled(true);
        
        when(repository.enable(id)).thenReturn(expected);

        ProductTagAssignmentModel result = useCase.enable(id);

        assertTrue(result.getEnabled());
        verify(repository).enable(id);
    }
}