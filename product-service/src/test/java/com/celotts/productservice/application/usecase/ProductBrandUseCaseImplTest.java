package com.celotts.productservice.application.usecase;

import com.celotts.productservice.application.usecase.product.ProductBrandUseCaseImpl;
import com.celotts.productservice.domain.exception.BrandNotFoundException;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductBrandUseCaseImplTest {

    private ProductBrandRepositoryPort repository;
    private ProductBrandUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductBrandRepositoryPort.class);
        useCase = new ProductBrandUseCaseImpl(repository);
    }

    @Test
    void save_ShouldSaveWhenBrandDoesNotExist() {
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(brand.getName()).thenReturn("Nike");

        when(repository.existsByName("Nike")).thenReturn(false);
        when(repository.save(brand)).thenReturn(brand);

        ProductBrandModel result = useCase.save(brand);

        assertNotNull(result);
        assertSame(brand, result);
        verify(repository).existsByName("Nike");
        verify(repository).save(brand);
    }

    @Test
    void save_ShouldThrowExceptionWhenBrandExists() {
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(brand.getName()).thenReturn("Adidas");

        when(repository.existsByName("Adidas")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.save(brand));
        assertEquals("Brand already exists", ex.getMessage());
        verify(repository).existsByName("Adidas");
        verify(repository, never()).save(any());
    }

    @Test
    void findById_ShouldReturnOptionalBrand() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(brand));

        Optional<ProductBrandModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertSame(brand, result.get());
        verify(repository).findById(id);
    }

    @Test
    void findByName_ShouldReturnOptionalBrand() {
        String name = "Puma";
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(repository.findByName(name)).thenReturn(Optional.of(brand));

        Optional<ProductBrandModel> result = useCase.findByName(name);

        assertTrue(result.isPresent());
        assertSame(brand, result.get());
        verify(repository).findByName(name);
    }

    @Test
    void findAll_ShouldReturnAllBrands() {
        List<ProductBrandModel> list = List.of(mock(ProductBrandModel.class), mock(ProductBrandModel.class));
        when(repository.findAll()).thenReturn(list);

        List<ProductBrandModel> result = useCase.findAll();

        assertEquals(list, result);
        verify(repository).findAll();
    }

    @Test
    void existsByName_ShouldReturnTrueIfExists() {
        when(repository.existsByName("Reebok")).thenReturn(true);
        assertTrue(useCase.existsByName("Reebok"));
        verify(repository).existsByName("Reebok");
    }

    @Test
    void deleteById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();

        useCase.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void existsById_ShouldReturnTrueIfExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        assertTrue(useCase.existsById(id));
        verify(repository).existsById(id);
    }

    @Test
    void findNameById_ShouldReturnNameIfPresent() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(brand.getName()).thenReturn("Under Armour");
        when(repository.findById(id)).thenReturn(Optional.of(brand));

        Optional<String> result = useCase.findNameById(id);

        assertEquals(Optional.of("Under Armour"), result);
        verify(repository).findById(id);
    }

    @Test
    void findAllIds_ShouldReturnIdsOfAllBrands() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        ProductBrandModel brand1 = mock(ProductBrandModel.class);
        ProductBrandModel brand2 = mock(ProductBrandModel.class);

        when(brand1.getId()).thenReturn(id1);
        when(brand2.getId()).thenReturn(id2);
        when(repository.findAll()).thenReturn(List.of(brand1, brand2));

        List<UUID> result = useCase.findAllIds();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(id1, id2)));
        verify(repository).findAll();
    }

    @Test
    void enableBrand_ShouldActivateBrandAndUpdateTimestampAndSave() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);

        when(repository.findById(id)).thenReturn(Optional.of(brand));
        when(repository.save(brand)).thenReturn(brand);

        ProductBrandModel result = useCase.enableBrand(id);

        assertSame(brand, result);
        verify(brand).activate();
        verify(brand).setUpdatedAt(any(LocalDateTime.class));
        verify(repository).save(brand);
    }

    @Test
    void enableBrand_ShouldThrowBrandNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> useCase.enableBrand(id));
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void disableBrand_ShouldDeactivateBrandAndUpdateTimestampAndSave() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);

        when(repository.findById(id)).thenReturn(Optional.of(brand));
        when(repository.save(brand)).thenReturn(brand);

        ProductBrandModel result = useCase.disableBrand(id);

        assertSame(brand, result);
        verify(brand).deactivate();
        verify(brand).setUpdatedAt(any(LocalDateTime.class));
        verify(repository).save(brand);
    }

    @Test
    void disableBrand_ShouldThrowBrandNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> useCase.disableBrand(id));
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }
}