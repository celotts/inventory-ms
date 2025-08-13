package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.BrandNotFoundException; // donde aplique

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.output.ProductBrandRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

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
        ProductBrandModel brand = new ProductBrandModel(
                UUID.randomUUID(),
                "Nike",
                "Marca deportiva",
                true,
                "admin",
                null,
                LocalDateTime.now(),
                null
        );

        when(repository.existsByName(brand.getName())).thenReturn(false);
        when(repository.save(brand)).thenReturn(brand);

        ProductBrandModel result = useCase.save(brand);

        assertNotNull(result);
        assertEquals(brand, result);
        verify(repository).existsByName(brand.getName());
        verify(repository).save(brand);
    }

    @Test
    void save_ShouldThrowExceptionWhenBrandExists() {
        ProductBrandModel brand = new ProductBrandModel(
                UUID.randomUUID(),
                "Adidas",
                "Marca alemana",
                true,
                "admin",
                null,
                LocalDateTime.now(),
                null
        );
        when(repository.existsByName(brand.getName())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.save(brand));
        assertEquals("Brand already exists", ex.getMessage());
        verify(repository).existsByName(brand.getName());
    }

    @Test
    void findById_ShouldReturnOptionalBrand() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(brand));

        Optional<ProductBrandModel> result = useCase.findById(id);

        assertTrue(result.isPresent());
        assertEquals(brand, result.get());
    }

    @Test
    void findByName_ShouldReturnOptionalBrand() {
        String name = "Puma";
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(repository.findByName(name)).thenReturn(Optional.of(brand));

        Optional<ProductBrandModel> result = useCase.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(brand, result.get());
    }

    @Test
    void findAll_ShouldReturnAllBrands() {
        List<ProductBrandModel> list = List.of(mock(ProductBrandModel.class));
        when(repository.findAll()).thenReturn(list);

        List<ProductBrandModel> result = useCase.findAll();

        assertEquals(list, result);
    }

    @Test
    void existsByName_ShouldReturnTrueIfExists() {
        String name = "Reebok";
        when(repository.existsByName(name)).thenReturn(true);

        assertTrue(useCase.existsByName(name));
    }

    @Test
    void deleteById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();
        doNothing().when(repository).deleteById(id);

        useCase.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void existsById_ShouldReturnTrueIfExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        assertTrue(useCase.existsById(id));
    }

    @Test
    void findNameById_ShouldReturnNameIfPresent() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(brand.getName()).thenReturn("Under Armour");
        when(repository.findById(id)).thenReturn(Optional.of(brand));

        Optional<String> result = useCase.findNameById(id);

        assertTrue(result.isPresent());
        assertEquals("Under Armour", result.get());
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
    }

    @Test
    void enableBrand_ShouldActivateBrand() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(brand));
        when(repository.save(any())).thenReturn(brand);

        ProductBrandModel result = useCase.enableBrand(id);

        assertNotNull(result);
        verify(brand).activate();
        verify(brand).setUpdatedAt(any(LocalDateTime.class));
        verify(repository).save(brand);
    }

    @Test
    void enableBrand_ShouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> useCase.enableBrand(id));
    }

    @Test
    void disableBrand_ShouldDeactivateBrand() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = mock(ProductBrandModel.class);
        when(repository.findById(id)).thenReturn(Optional.of(brand));
        when(repository.save(any())).thenReturn(brand);

        ProductBrandModel result = useCase.disableBrand(id);

        assertNotNull(result);
        verify(brand).deactivate();
        verify(brand).setUpdatedAt(any(LocalDateTime.class));
        verify(repository).save(brand);
    }

    @Test
    void disableBrand_ShouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> useCase.disableBrand(id));
    }
}