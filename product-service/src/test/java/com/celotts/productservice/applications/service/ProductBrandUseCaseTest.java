package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.ProductBrandUseCaseImpl;
import com.celotts.productservice.domain.exception.BrandNotFoundException;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.output.ProductBrandRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBrandUseCaseTest {

    @Mock
    private ProductBrandRepositoryPort repository;

    @InjectMocks
    private ProductBrandUseCaseImpl useCase;

    private ProductBrandModel createModel(String name) {
        return ProductBrandModel.builder()
                .name(name)
                .description("desc")
                .enabled(true)
                .createdBy("tester")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static ProductBrandModel model(UUID id, String name) {
        return ProductBrandModel.builder()
                .id(id)
                .name(name)
                .enabled(true)
                .build();
    }

    // -------- create --------
    @Test
    @DisplayName("create: guarda cuando el nombre no existe")
    void create_ok() {
        ProductBrandModel input = createModel("BrandX");

        when(repository.existsByName("BrandX")).thenReturn(false);
        ProductBrandModel saved = model(UUID.randomUUID(), "BrandX");
        when(repository.save(input)).thenReturn(saved);

        ProductBrandModel result = useCase.create(input);

        assertSame(saved, result);
        verify(repository).save(input);
    }

    @Test
    @DisplayName("create: lanza IllegalArgumentException cuando el nombre ya existe")
    void create_nameExists_throws() {
        ProductBrandModel input = createModel("BrandX");

        when(repository.existsByName("BrandX")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.create(input));
        verify(repository, never()).save(any());
    }

    // -------- findAll / findById --------
    @Test
    void findAll_returnsList() {
        ProductBrandModel m1 = model(UUID.randomUUID(), "A");
        ProductBrandModel m2 = model(UUID.randomUUID(), "B");
        when(repository.findAll()).thenReturn(List.of(m1, m2));

        List<ProductBrandModel> list = useCase.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void findById_found_returnsOptional() {
        UUID id = UUID.randomUUID();
        ProductBrandModel m = model(id, "C");
        when(repository.findById(id)).thenReturn(Optional.of(m));

        Optional<ProductBrandModel> opt = useCase.findById(id);
        assertTrue(opt.isPresent());
        assertSame(m, opt.get());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<ProductBrandModel> opt = useCase.findById(id);
        assertTrue(opt.isEmpty());
    }

    // -------- update --------
    @Test
    void update_ok_updatesAndSaves() {
        UUID id = UUID.randomUUID();
        ProductBrandModel existing = model(id, "Old");
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        ProductBrandModel patch = ProductBrandModel.builder()
                .name("New")
                .description("new-desc")
                .enabled(false)
                .updatedBy("upd")
                .build();

        ProductBrandModel saved = model(id, "New");
        saved.setDescription("new-desc");
        saved.setEnabled(false);
        saved.setUpdatedBy("upd");
        when(repository.save(existing)).thenReturn(saved);

        ProductBrandModel result = useCase.update(id, patch);

        assertSame(saved, result);
        assertEquals("New", existing.getName());
        assertEquals("new-desc", existing.getDescription());
        assertFalse(existing.getEnabled());
        assertEquals("upd", existing.getUpdatedBy());
        assertNotNull(existing.getUpdatedAt());
        verify(repository).save(existing);
    }

    // -------- delete --------
    @Test
    void delete_notExists_throws() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);
        assertThrows(BrandNotFoundException.class, () -> useCase.delete(id));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void delete_ok_callsRepository() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        useCase.delete(id);
        verify(repository).deleteById(id);
    }

    // -------- findByName / exists --------
    @Test
    void findByName_found_returnsOptional() {
        ProductBrandModel m = model(UUID.randomUUID(), "Nike");
        when(repository.findByName("Nike")).thenReturn(Optional.of(m));

        Optional<ProductBrandModel> opt = useCase.findByName("Nike");
        assertTrue(opt.isPresent());
        assertSame(m, opt.get());
    }

    @Test
    void findByName_notFound_returnsEmpty() {
        when(repository.findByName("Nope")).thenReturn(Optional.empty());
        assertTrue(useCase.findByName("Nope").isEmpty());
    }

    @Test
    void existsByName_delegates() {
        when(repository.existsByName("ACME")).thenReturn(true);
        assertTrue(useCase.existsByName("ACME"));
    }

    @Test
    void existsById_delegates() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        assertTrue(useCase.existsById(id));
    }

    @Test
    void findNameById_delegates() {
        UUID id = UUID.randomUUID();
        ProductBrandModel m = model(id, "Brand");
        when(repository.findById(id)).thenReturn(Optional.of(m));

        Optional<String> name = useCase.findNameById(id);
        assertEquals(Optional.of("Brand"), name);
    }

    @Test
    void findAllIds_delegates() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        ProductBrandModel m1 = model(a, "A");
        ProductBrandModel m2 = model(b, "B");
        when(repository.findAll()).thenReturn(List.of(m1, m2));

        assertEquals(List.of(a, b), useCase.findAllIds());
    }

    // -------- enable / disable --------
    @Test
    void enableBrand_updatesAndSaves() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = model(id, "Z");
        brand.setEnabled(false);
        when(repository.findById(id)).thenReturn(Optional.of(brand));
        when(repository.save(brand)).thenReturn(brand);

        ProductBrandModel result = useCase.enableBrand(id);

        assertTrue(result.getEnabled());
        verify(repository).save(brand);
    }

    @Test
    void disableBrand_updatesAndSaves() {
        UUID id = UUID.randomUUID();
        ProductBrandModel brand = model(id, "Z");
        brand.setEnabled(true);
        when(repository.findById(id)).thenReturn(Optional.of(brand));
        when(repository.save(brand)).thenReturn(brand);

        ProductBrandModel result = useCase.disableBrand(id);

        assertFalse(result.getEnabled());
        verify(repository).save(brand);
    }
}