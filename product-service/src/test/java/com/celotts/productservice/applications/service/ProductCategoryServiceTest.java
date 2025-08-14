package com.celotts.productservice.applications.service;

import com.celotts.productserviceOld.applications.service.ProductCategoryService;
import com.celotts.productserviceOld.domain.model.ProductCategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.product.ProductCategoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryAdapter adapter;

    private ProductCategoryService service;

    private UUID id;
    private UUID productId;
    private UUID categoryId;
    private ProductCategoryModel model;
    private ProductCategoryCreateDto dto;

    @BeforeEach
    void setUp() {
        service = new ProductCategoryService(adapter);

        id = UUID.randomUUID();
        productId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        dto = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(LocalDateTime.now())
                .enabled(true)
                .createdBy("test-user")
                .updatedBy("test-user")
                .build();

        model = ProductCategoryModel.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(dto.getAssignedAt())
                .enabled(dto.getEnabled())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void assignCategoryToProduct_shouldConvertDtoAndSave() {
        when(adapter.save(any())).thenReturn(model);

        ProductCategoryModel result = service.assignCategoryToProduct(dto);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(categoryId, result.getCategoryId());
        verify(adapter).save(any(ProductCategoryModel.class));
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void getById_shouldReturnModel() {
        when(adapter.getById(id)).thenReturn(model);

        ProductCategoryModel result = service.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(adapter).getById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void getAll_shouldReturnList() {
        when(adapter.getAll()).thenReturn(List.of(model));

        List<ProductCategoryModel> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(adapter).getAll();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void deleteById_shouldCallAdapter() {
        service.deleteById(id);

        verify(adapter).deleteById(id);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void disableById_shouldCallAdapter() {
        service.disableById(id);

        verify(adapter).disableById(id);
    }
}