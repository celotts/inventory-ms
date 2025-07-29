package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.CategoryService;
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private AutoCloseable closeable;
    private UUID categoryId;
    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        categoryId = UUID.randomUUID();
        categoryModel = CategoryModel.builder()
                .id(categoryId)
                .name("Test Category")
                .description("Desc")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void toggleCategoryStatus_shouldReturnUpdatedCategory() {
        when(categoryService.updateStatus(categoryId, false)).thenReturn(categoryModel.toBuilder().active(false).build());

        ResponseEntity<?> response = categoryController.toggleCategoryStatus(categoryId, false);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(false, response.getBody().toString().contains("true")); // rudimentary check
    }

    @Test
    void toggleCategoryStatus_shouldReturnNotFound() {
        when(categoryService.updateStatus(categoryId, false)).thenReturn(null);

        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            categoryController.toggleCategoryStatus(categoryId, false);
        });
    }

    @Test
    void restoreCategory_shouldReturnRestoredCategory() {
        when(categoryService.restore(categoryId)).thenReturn(categoryModel);

        ResponseEntity<?> response = categoryController.restoreCategory(categoryId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void getPaginatedCategories_shouldReturnPage() {
        when(categoryService.findAllPaginated(any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(categoryModel)));

        ResponseEntity<?> response = categoryController.getPaginatedCategories(null, null, Pageable.ofSize(1));
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getCategoryStats_shouldReturnStats() {
        CategoryStatsDto stats = new CategoryStatsDto(
                10L, 7L, 2L, 1L,
                70.0, 20.0, 10.0
        );

        when(categoryService.getCategoryStatistics()).thenReturn(stats);

        ResponseEntity<?> response = categoryController.getCategoryStats();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stats, response.getBody());
    }

    @Test
    void existsByName_shouldReturnTrueOrFalse() {
        when(categoryService.existsByName("Existing")).thenReturn(true);
        ResponseEntity<Boolean> exists = categoryController.categoryExists("Existing");
        assertTrue(exists.getBody());

        when(categoryService.existsByName("Missing")).thenReturn(false);
        exists = categoryController.categoryExists("Missing");
        assertFalse(exists.getBody());
    }

    @Test
    void getActiveCategories_shouldReturnOnlyActive() {
        when(categoryService.findAll()).thenReturn(List.of(categoryModel));

        ResponseEntity<?> response = categoryController.getActiveCategories();
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void searchCategories_shouldReturnMatching() {
        when(categoryService.searchByNameOrDescription("Test", 10)).thenReturn(List.of(categoryModel));

        ResponseEntity<?> response = categoryController.searchCategories("Test", 10);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void permanentDeleteCategory_shouldCallService() {
        ResponseEntity<Void> response = categoryController.permanentDeleteCategory(categoryId);

        verify(categoryService).permanentDelete(categoryId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() {
        CategoryCreateDto createDto = new CategoryCreateDto("Nueva Categoría", "Descripción");
        CategoryModel createdModel = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name(createDto.getName())
                .description(createDto.getDescription())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryService.create(any(CategoryModel.class))).thenReturn(createdModel);

        ResponseEntity<?> response = categoryController.createCategory(createDto);

        assertEquals(201, response.getStatusCodeValue());
        Object responseBody = response.getBody();
        assertNotNull(responseBody);
        System.out.println("Response body type: " + responseBody.getClass());

        String bodyString = responseBody.toString();
        assertTrue(bodyString.contains("Nueva Categoría"));
        assertTrue(bodyString.contains("Descripción"));
    }
}