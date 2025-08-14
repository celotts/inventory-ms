package com.celotts.productservice.applications.usecase;

import com.celotts.productserviceOld.applications.usecase.ProductUnitUseCaseImpl;
import com.celotts.productserviceOld.domain.model.ProductUnitModel;
import com.celotts.productserviceOld.domain.port.product.unit.output.ProductUnitRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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

    @Test
    void testSave() {
        ProductUnitModel model = mock(ProductUnitModel.class);
        when(repository.save(model)).thenReturn(model);

        ProductUnitModel result = useCase.save(model);

        assertNotNull(result);
        assertEquals(model, result);
        verify(repository).save(model);
    }

    @Test
    void testFindById_whenFound() {
        UUID id = UUID.randomUUID();
        ProductUnitModel model = mock(ProductUnitModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(model));

        Optional<ProductUnitModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertEquals(model, result.get());
        verify(repository).findById(id);
    }

    @Test
    void testFindById_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<ProductUnitModel> result = useCase.findById(id);

        assertFalse(result.isPresent());
        verify(repository).findById(id);
    }

    @Test
    void testFindAll() {
        List<ProductUnitModel> list = List.of(mock(ProductUnitModel.class));
        when(repository.findAll()).thenReturn(list);

        List<ProductUnitModel> result = useCase.findAll();

        assertEquals(list.size(), result.size());
        verify(repository).findAll();
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);

        useCase.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void testExistsById_whenExists() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(mock(ProductUnitModel.class)));

        boolean exists = useCase.existsById(id);

        assertTrue(exists);
        verify(repository).findById(id);
    }

    @Test
    void testExistsById_whenNotExists() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean exists = useCase.existsById(id);

        assertFalse(exists);
        verify(repository).findById(id);
    }

    @Test
    void testExistsByCode() {
        String code = "KG";
        when(repository.existsByCode(code)).thenReturn(true);

        boolean result = useCase.existsByCode(code);

        assertTrue(result);
        verify(repository).existsByCode(code);
    }

    @Test
    void testFindNameByCode() {
        String code = "LTS";
        when(repository.findNameByCode(code)).thenReturn(Optional.of("Litros"));

        Optional<String> result = useCase.findNameByCode(code);

        assertTrue(result.isPresent());
        assertEquals("Litros", result.get());
        verify(repository).findNameByCode(code);
    }

    @Test
    void testFindAllCodes() {
        List<String> codes = List.of("KG", "LTS");
        when(repository.findAllCodes()).thenReturn(codes);

        List<String> result = useCase.findAllCodes();

        assertEquals(codes, result);
        verify(repository).findAllCodes();
    }
}