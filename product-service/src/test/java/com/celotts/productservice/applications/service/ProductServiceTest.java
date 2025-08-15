package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.service.ProductService;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productservice.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @Mock
    private ProductRepositoryPort repository;

    @Mock
    private ProductUnitRepositoryPort unitPort;

    @Mock
    private ProductBrandPort brandPort;

    @Mock
    private CategoryRepositoryPort categoryPort;

    @Mock
    private ProductRequestMapper mapper;

    private ProductService productService;

    private ProductModel product;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(repository, unitPort, brandPort, categoryPort, mapper);
        product = ProductModel.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .description("Desc")
                .code("CODE01")
                .unitCode("UNIT01")
                .categoryId(UUID.randomUUID())
                .brandId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(3)
                .unitPrice(BigDecimal.valueOf(10.5))
                .enabled(true)
                .build();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetAllProducts() {
        when(repository.findAll()).thenReturn(List.of(product));
        List<ProductModel> result = productService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetPaginatedProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(product)));
        Page<ProductModel> result = productService.getAll(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetProductsWithFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllWithFilters(pageable, "C", "N", "D"))
                .thenReturn(new PageImpl<>(List.of(product)));
        Page<ProductModel> result = productService.getAllWithFilters(pageable, "C", "N", "D");
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetDisabledProducts() {
        when(repository.findByEnabled(false, Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(product.withEnabled(false))));
        List<ProductModel> result = productService.getDisabledProducts();
        assertEquals(1, result.size());
        assertFalse(result.get(0).getEnabled());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetByCategory() {
        UUID categoryId = product.getCategoryId();
        when(repository.findByCategoryId(categoryId)).thenReturn(List.of(product));
        List<ProductModel> result = productService.getByCategory(categoryId);
        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetLowStockByCategory() {
        UUID categoryId = product.getCategoryId();
        when(repository.findByCategoryId(categoryId)).thenReturn(List.of(product));
        List<ProductModel> result = productService.getLowStockByCategory(categoryId);
        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetLowStockProducts() {
        when(repository.findAll()).thenReturn(List.of(product));
        List<ProductModel> result = productService.getLowStockProducts();
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetByBrand() {
        UUID brandId = product.getBrandId();
        when(repository.findByBrandId(brandId)).thenReturn(List.of(product));
        List<ProductModel> result = productService.getByBrand(brandId);
        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldUpdateStock() {
        UUID id = product.getId();
        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(repository.save(any())).thenReturn(product.withCurrentStock(99));
        ProductModel result = productService.updateStock(id, 99);
        assertEquals(99, result.getCurrentStock());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldCountProducts() {
        when(repository.findAll()).thenReturn(List.of(product));
        assertEquals(1, productService.count());
    }

    @Test
    void shouldCountEnabledProducts() {
        Page<ProductModel> page = new PageImpl<>(List.of(product));
        when(repository.findByEnabled(true, Pageable.unpaged())).thenReturn(page);
        assertEquals(1, productService.countEnabled());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldCreateProduct() {
        when(repository.save(product)).thenReturn(product);
        ProductModel result = productService.create(product);
        assertEquals(product, result);
        verify(repository).save(product);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldUpdateProduct() {
        UUID id = product.getId();
        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(repository.save(any())).thenReturn(product.withName("Updated"));

        ProductModel result = productService.update(id, product.withName("Updated"));
        assertEquals("Updated", result.getName());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetProductById() {
        UUID id = product.getId();
        when(repository.findById(id)).thenReturn(Optional.of(product));
        ProductModel result = productService.getProductById(id);
        assertEquals(product, result);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldThrowWhenProductByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> productService.getProductById(id));
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetProductByCode() {
        when(repository.findByCode("CODE01")).thenReturn(Optional.of(product));
        ProductModel result = productService.getProductByCode("CODE01");
        assertEquals(product, result);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldThrowWhenProductByCodeNotFound() {
        when(repository.findByCode("XXX")).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> productService.getProductByCode("XXX"));
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldEnableProduct() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product.withEnabled(false)));
        when(repository.save(any())).thenReturn(product.withEnabled(true));
        ProductModel result = productService.enable(product.getId());
        assertTrue(result.getEnabled());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldDisableProduct() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        productService.disable(product.getId());
        verify(repository).save(any());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldCheckExistsById() {
        when(repository.existsById(product.getId())).thenReturn(true);
        assertTrue(productService.existsById(product.getId()));
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldCheckExistsByCode() {
        when(repository.findByCode("CODE01")).thenReturn(Optional.of(product));
        assertTrue(productService.existsByCode("CODE01"));
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldValidateUnitCodeExists() {
        when(unitPort.existsByCode("UNIT01")).thenReturn(true);
        when(unitPort.findNameByCode("UNIT01")).thenReturn(Optional.of("Kilogramo"));
        Optional<String> result = productService.validateUnitCode("UNIT01");
        assertTrue(result.isPresent());
        assertEquals("Kilogramo", result.get());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldValidateUnitCodeNotExists() {
        when(unitPort.existsByCode("X")).thenReturn(false);
        Optional<String> result = productService.validateUnitCode("X");
        assertTrue(result.isEmpty());
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldDeleteProductById() {
        UUID id = product.getId();

        // No se espera retorno, solo verificación de la invocación
        productService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void shouldGetEnabledProductsPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductModel> expectedPage = new PageImpl<>(List.of(product));

        when(repository.findByEnabled(true, pageable)).thenReturn(expectedPage);

        Page<ProductModel> result = productService.getEnabledProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getEnabled());
    }
}