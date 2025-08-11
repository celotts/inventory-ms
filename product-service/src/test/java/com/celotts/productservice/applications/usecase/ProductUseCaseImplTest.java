package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productservice.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreate;
import com.celotts.productservice.domain.model.ProductReference;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUseCaseImplTest {

    private ProductRepositoryPort productRepositoryPort;
    private ProductUnitRepositoryPort productUnitPort;
    private ProductBrandPort productBrandPort;
    private CategoryRepositoryPort categoryRepositoryPort;
    private ProductRequestMapper productRequestMapper;

    private ProductUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        productUnitPort = mock(ProductUnitRepositoryPort.class);
        productBrandPort = mock(ProductBrandPort.class);
        categoryRepositoryPort = mock(CategoryRepositoryPort.class);
        productRequestMapper = mock(ProductRequestMapper.class);

        useCase = new ProductUseCaseImpl(
                productRepositoryPort,
                productUnitPort,
                productBrandPort,
                categoryRepositoryPort,
                productRequestMapper
        );
    }

    // ---------- helpers ----------
    private ProductCreate createDto(String code, UUID brandId, UUID categoryId, String unitCode) {
        ProductCreate dto = mock(ProductCreate.class);
        when(dto.getCode()).thenReturn(code);
        when(dto.getBrandId()).thenReturn(brandId);
        when(dto.getCategoryId()).thenReturn(categoryId);
        when(dto.getUnitCode()).thenReturn(unitCode);
        return dto;
    }

    private ProductUpdateDto updateDto(UUID brandId, UUID categoryId, String unitCode) {
        ProductUpdateDto dto = mock(ProductUpdateDto.class);
        when(dto.getBrandId()).thenReturn(brandId);
        when(dto.getCategoryId()).thenReturn(categoryId);
        when(dto.getUnitCode()).thenReturn(unitCode);
        return dto;
    }

    private ProductReference refs(String unitCode, UUID brandId, UUID categoryId) {
        ProductReference dto = mock(ProductReference.class);
        when(dto.getUnitCode()).thenReturn(unitCode);
        when(dto.getBrandId()).thenReturn(brandId);
        when(dto.getCategoryId()).thenReturn(categoryId);
        return dto;
    }

    // ---------- createProduct ----------
    @Test
    @DisplayName("createProduct lanza ProductAlreadyExistsException si el código ya existe")
    void createProduct_codeExists_throws() {
        ProductCreate dto = createDto("P-1", UUID.randomUUID(), UUID.randomUUID(), "KG");
        when(productRepositoryPort.findByCode("P-1")).thenReturn(Optional.of(mock(ProductModel.class)));

        assertThrows(ProductAlreadyExistsException.class, () -> useCase.createProduct(dto));
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("createProduct valida referencias, mapea y guarda")
    void createProduct_ok_saves() {
        ProductCreate dto = createDto("P-2", UUID.randomUUID(), UUID.randomUUID(), "KG");

        when(productRepositoryPort.findByCode("P-2")).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode("KG")).thenReturn(true);
        when(productBrandPort.existsById(dto.getBrandId())).thenReturn(true);
        when(categoryRepositoryPort.existsById(dto.getCategoryId())).thenReturn(true);

        ProductModel mapped = mock(ProductModel.class);
        when(productRequestMapper.toModel(dto)).thenReturn(mapped);

        ProductModel saved = mock(ProductModel.class);
        when(productRepositoryPort.save(mapped)).thenReturn(saved);

        ProductModel result = useCase.createProduct(dto);

        assertSame(saved, result);
        verify(productRequestMapper).toModel(dto);
        verify(productRepositoryPort).save(mapped);
    }

    // ---------- updateProduct ----------
    @Test
    @DisplayName("updateProduct lanza 404 si el ID no existe")
    void updateProduct_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.updateProduct(id, updateDto(UUID.randomUUID(), UUID.randomUUID(), "KG")));
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("updateProduct valida referencias, aplica cambios y guarda")
    void updateProduct_ok_updatesAndSaves() {
        UUID id = UUID.randomUUID();
        ProductUpdateDto dto = updateDto(UUID.randomUUID(), UUID.randomUUID(), "KG");

        ProductModel existing = mock(ProductModel.class);
        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(existing));
        when(productUnitPort.existsByCode("KG")).thenReturn(true);
        when(productBrandPort.existsById(dto.getBrandId())).thenReturn(true);
        when(categoryRepositoryPort.existsById(dto.getCategoryId())).thenReturn(true);

        doNothing().when(productRequestMapper).updateModelFromDto(existing, dto);

        ProductModel saved = mock(ProductModel.class);
        when(productRepositoryPort.save(existing)).thenReturn(saved);

        ProductModel result = useCase.updateProduct(id, dto);

        assertSame(saved, result);
        verify(productRequestMapper).updateModelFromDto(existing, dto);
        verify(productRepositoryPort).save(existing);
    }

    // ---------- getProductById / getProductByCode ----------
    @Test
    void getProductById_found_returns() {
        UUID id = UUID.randomUUID();
        ProductModel m = mock(ProductModel.class);
        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(m));

        assertSame(m, useCase.getProductById(id));
    }

    @Test
    void getProductById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> useCase.getProductById(id));
    }

    @Test
    void getProductByCode_found_returns() {
        ProductModel m = mock(ProductModel.class);
        when(productRepositoryPort.findByCode("C-1")).thenReturn(Optional.of(m));
        assertSame(m, useCase.getProductByCode("C-1"));
    }

    @Test
    void getProductByCode_notFound_throws() {
        when(productRepositoryPort.findByCode("C-2")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> useCase.getProductByCode("C-2"));
    }

    // ---------- enable / disable / stock ----------
    @Test
    void enableProduct_setsEnabledTrue_andSaves() {
        UUID id = UUID.randomUUID();
        ProductModel existing = mock(ProductModel.class);
        ProductModel enabled = mock(ProductModel.class);

        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(existing));
        when(existing.withEnabled(true)).thenReturn(enabled);
        when(productRepositoryPort.save(enabled)).thenReturn(enabled);

        ProductModel result = useCase.enableProduct(id);
        assertSame(enabled, result);
        verify(productRepositoryPort).save(enabled);
    }

    @Test
    void disableProduct_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> useCase.disableProduct(id));
    }

    @Test
    void disableProduct_setsEnabledFalse_andSaves() {
        UUID id = UUID.randomUUID();
        ProductModel existing = mock(ProductModel.class);
        ProductModel disabled = mock(ProductModel.class);

        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(existing));
        when(existing.withEnabled(false)).thenReturn(disabled);
        when(productRepositoryPort.save(disabled)).thenReturn(disabled);

        ProductModel result = useCase.disableProduct(id);
        assertSame(disabled, result);
        verify(productRepositoryPort).save(disabled);
    }

    @Test
    void updateStock_setsCurrentStock_andSaves() {
        UUID id = UUID.randomUUID();
        ProductModel existing = mock(ProductModel.class);
        ProductModel updated = mock(ProductModel.class);

        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(existing));
        when(existing.withCurrentStock(5)).thenReturn(updated);
        when(productRepositoryPort.save(updated)).thenReturn(updated);

        ProductModel result = useCase.updateStock(id, 5);
        assertSame(updated, result);
        verify(productRepositoryPort).save(updated);
    }

    // ---------- paginación y filtros ----------
    @Test
    void getAllProducts_delegates() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductModel> page = new PageImpl<>(List.of(mock(ProductModel.class)), pageable, 1);
        when(productRepositoryPort.findAll(pageable)).thenReturn(page);

        assertSame(page, useCase.getAllProducts(pageable));
    }

    @Test
    void getAllProductsWithFilters_delegates() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ProductModel> page = new PageImpl<>(List.of(), pageable, 0);
        when(productRepositoryPort.findAllWithFilters(pageable, "C", "N", "D")).thenReturn(page);

        assertSame(page, useCase.getAllProductsWithFilters(pageable, "C", "N", "D"));
    }

    @Test
    void getActiveProducts_delegates() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductModel> page = new PageImpl<>(List.of());
        when(productRepositoryPort.findByEnabled(true, pageable)).thenReturn(page);

        assertSame(page, useCase.getActiveProducts(pageable));
    }

    @Test
    void getInactiveProducts_returnsContent() {
        Page<ProductModel> page = new PageImpl<>(List.of(mock(ProductModel.class), mock(ProductModel.class)));
        when(productRepositoryPort.findByEnabled(false, Pageable.unpaged())).thenReturn(page);

        List<ProductModel> result = useCase.getInactiveProducts();
        assertEquals(2, result.size());
    }

    @Test
    void getProductsByCategory_delegates() {
        UUID cat = UUID.randomUUID();
        List<ProductModel> list = List.of(mock(ProductModel.class));
        when(productRepositoryPort.findByCategoryId(cat)).thenReturn(list);

        assertSame(list, useCase.getProductsByCategory(cat));
    }

    @Test
    void getProductsByBrand_delegates() {
        UUID brand = UUID.randomUUID();
        List<ProductModel> list = List.of(mock(ProductModel.class));
        when(productRepositoryPort.findByBrandId(brand)).thenReturn(list);

        assertSame(list, useCase.getProductsByBrand(brand));
    }

    // ---------- low stock ----------
    @Test
    void getLowStockByCategory_filtersByLowStock() {
        UUID cat = UUID.randomUUID();
        ProductModel low = mock(ProductModel.class);
        ProductModel high = mock(ProductModel.class);
        when(low.lowStock()).thenReturn(true);
        when(high.lowStock()).thenReturn(false);
        when(productRepositoryPort.findByCategoryId(cat)).thenReturn(List.of(low, high));

        List<ProductModel> result = useCase.getLowStockByCategory(cat);
        assertEquals(1, result.size());
        assertSame(low, result.get(0));
    }

    @Test
    void getLowStockProducts_filtersByLowStock() {
        ProductModel low1 = mock(ProductModel.class);
        ProductModel low2 = mock(ProductModel.class);
        ProductModel ok = mock(ProductModel.class);
        when(low1.lowStock()).thenReturn(true);
        when(low2.lowStock()).thenReturn(true);
        when(ok.lowStock()).thenReturn(false);
        when(productRepositoryPort.findAll()).thenReturn(List.of(low1, ok, low2));

        List<ProductModel> result = useCase.getLowStockProducts();
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(low1, low2)));
    }

    // ---------- counts ----------
    @Test
    void countProducts_returnsSizeOfFindAll() {
        when(productRepositoryPort.findAll()).thenReturn(List.of(mock(ProductModel.class), mock(ProductModel.class)));
        assertEquals(2, useCase.countProducts());
    }

    @Test
    void countActiveProducts_returnsTotalElements() {
        Page<ProductModel> page = new PageImpl<>(List.of(), Pageable.unpaged(), 7);
        when(productRepositoryPort.findByEnabled(true, Pageable.unpaged())).thenReturn(page);

        assertEquals(7L, useCase.countActiveProducts());
    }

    // ---------- validateUnitCode / exists / getAll ----------
    @Test
    void validateUnitCode_notExists_returnsEmpty() {
        when(productUnitPort.existsByCode("KG")).thenReturn(false);
        assertTrue(useCase.validateUnitCode("KG").isEmpty());
        verify(productUnitPort, never()).findNameByCode(anyString());
    }

    @Test
    void validateUnitCode_exists_returnsName() {
        when(productUnitPort.existsByCode("KG")).thenReturn(true);
        when(productUnitPort.findNameByCode("KG")).thenReturn(Optional.of("Kilogram"));
        assertEquals(Optional.of("Kilogram"), useCase.validateUnitCode("KG"));
    }

    @Test
    void existsById_delegates() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.existsById(id)).thenReturn(true);
        assertTrue(useCase.existsById(id));
    }

    @Test
    void existsByCode_true_whenFound() {
        when(productRepositoryPort.findByCode("C-9")).thenReturn(Optional.of(mock(ProductModel.class)));
        assertTrue(useCase.existsByCode("C-9"));
    }

    @Test
    void getAll_delegates() {
        List<ProductModel> list = List.of(mock(ProductModel.class));
        when(productRepositoryPort.findAll()).thenReturn(list);
        assertSame(list, useCase.getAll());
    }

    // ---------- validateReferences - errores específicos ----------
    @Test
    void validateReferences_invalidUnit_throws() {
        String code = "NEVER-USED";
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductCreate create = mock(ProductCreate.class);
        when(create.getCode()).thenReturn(code);
        when(create.getBrandId()).thenReturn(brandId);
        when(create.getCategoryId()).thenReturn(categoryId);
        when(create.getUnitCode()).thenReturn("BAD");

        when(productRepositoryPort.findByCode(code)).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode("BAD")).thenReturn(false);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> useCase.createProduct(create));
        assertTrue(ex.getMessage().contains("Invalid unit code"));
    }

    @Test
    void validateReferences_invalidBrand_throws() {
        String code = "NEVER-USED";
        String unit = "KG";
        UUID brand = UUID.randomUUID();
        UUID category = UUID.randomUUID();

        ProductCreate create = mock(ProductCreate.class);
        when(create.getCode()).thenReturn(code);
        when(create.getUnitCode()).thenReturn(unit);
        when(create.getBrandId()).thenReturn(brand);
        when(create.getCategoryId()).thenReturn(category);

        when(productRepositoryPort.findByCode(code)).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode(unit)).thenReturn(true);
        when(productBrandPort.existsById(brand)).thenReturn(false);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> useCase.createProduct(create));
        assertTrue(ex.getMessage().contains("Invalid brand ID"));
    }

    @Test
    void validateReferences_invalidCategory_throws() {
        String code = "NEVER-USED";
        String unit = "KG";
        UUID brand = UUID.randomUUID();
        UUID category = UUID.randomUUID();

        ProductCreate create = mock(ProductCreate.class);
        when(create.getCode()).thenReturn(code);
        when(create.getUnitCode()).thenReturn(unit);
        when(create.getBrandId()).thenReturn(brand);
        when(create.getCategoryId()).thenReturn(category);

        when(productRepositoryPort.findByCode(code)).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode(unit)).thenReturn(true);
        when(productBrandPort.existsById(brand)).thenReturn(true);
        when(categoryRepositoryPort.existsById(category)).thenReturn(false);

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> useCase.createProduct(create));
        assertTrue(ex.getMessage().contains("Invalid category ID"));
    }


}