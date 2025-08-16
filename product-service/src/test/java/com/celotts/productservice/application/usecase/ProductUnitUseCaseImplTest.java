package com.celotts.productservice.application.usecase;

import com.celotts.productservice.application.usecase.product.ProductUnitUseCaseImpl;
import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUnitUseCaseImplTest {

    private ProductUnitRepositoryPort repository;
    private ProductUnitUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductUnitRepositoryPort.class);
        useCase = new ProductUnitUseCaseImpl(repository);
    }

    // --- save ---
    @Test
    void save_returnsSavedModel() {
        ProductUnitModel model = mock(ProductUnitModel.class);
        when(repository.save(model)).thenReturn(model);

        ProductUnitModel result = useCase.save(model);

        assertNotNull(result);
        assertSame(model, result);
        verify(repository).save(model);
    }

    // --- findById ---
    @Test
    void findById_found_returnsOptionalWithValue() {
        UUID id = UUID.randomUUID();
        ProductUnitModel model = mock(ProductUnitModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(model));

        Optional<ProductUnitModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertSame(model, result.get());
        verify(repository).findById(id);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<ProductUnitModel> result = useCase.findById(id);

        assertTrue(result.isEmpty());
        verify(repository).findById(id);
    }

    // --- findAll ---
    @Test
    void findAll_delegatesToRepository() {
        List<ProductUnitModel> list = List.of(mock(ProductUnitModel.class));
        when(repository.findAll()).thenReturn(list);

        List<ProductUnitModel> result = useCase.findAll();

        assertEquals(list, result);
        verify(repository).findAll();
    }

    // --- deleteById ---
    @Test
    void deleteById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);

        useCase.deleteById(id);

        verify(repository).deleteById(id);
    }

    // --- existsById ---
    @Test
    void existsById_true_whenFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(mock(ProductUnitModel.class)));

        boolean exists = useCase.existsById(id);

        assertTrue(exists);
        // no verifico método exacto para no acoplar al detalle de implementación
    }

    @Test
    void existsById_false_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean exists = useCase.existsById(id);

        assertFalse(exists);
    }

    // --- existsByCode ---
    @Test
    void existsByCode_true_whenRepoSaysTrue() {
        String code = "KG";
        when(repository.existsByCode(code)).thenReturn(true);

        assertTrue(useCase.existsByCode(code));
        verify(repository).existsByCode(code);
    }

    // --- findNameByCode ---
    @Test
    void findNameByCode_present_returnsValue() {
        String code = "LTS";
        when(repository.findNameByCode(code)).thenReturn(Optional.of("Litros"));

        Optional<String> result = useCase.findNameByCode(code);

        assertTrue(result.isPresent());
        assertEquals("Litros", result.get());
        verify(repository).findNameByCode(code);
    }

    // --- findAllCodes ---
    @Test
    void findAllCodes_delegatesToRepository() {
        List<String> codes = List.of("KG", "LTS");
        when(repository.findAllCodes()).thenReturn(codes);

        List<String> result = useCase.findAllCodes();

        assertEquals(codes, result);
        verify(repository).findAllCodes();
    }
}