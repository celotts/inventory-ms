package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;


import com.celotts.productservice.applications.service.CategoryService;
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryDeleteDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        assertEquals(200, response.getStatusCode().value());
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

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getPaginatedCategories_shouldReturnPage() {
        Pageable pageable = Pageable.unpaged(); // Compatible con Java 17
        PageImpl<CategoryModel> page = new PageImpl<>(List.of(categoryModel));

        when(categoryService.findAllPaginated(any(), any(), eq(pageable)))
                .thenReturn(page);

        ResponseEntity<?> response = categoryController.getPaginatedCategories(null, null, pageable);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getCategoryStats_shouldReturnStats() {
        CategoryStatusDto stats = new CategoryStatusDto(
                10L, 7L, 2L, 1L,
                70.0, 20.0, 10.0
        );

        when(categoryService.getCategoryStatistics()).thenReturn(stats);

        ResponseEntity<?> response = categoryController.getCategoryStats();

        assertEquals(200, response.getStatusCode().value());
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
        assertEquals(200, response.getStatusCode().value());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void searchCategories_shouldReturnMatching() {
        when(categoryService.searchByNameOrDescription("Test", 10)).thenReturn(List.of(categoryModel));

        ResponseEntity<?> response = categoryController.searchCategories("Test", 10);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void permanentDeleteCategory_shouldCallService() {
        ResponseEntity<Void> response = categoryController.permanentDeleteCategory(categoryId);

        verify(categoryService).permanentDelete(categoryId);
        assertEquals(204, response.getStatusCode().value());
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

        assertEquals(201, response.getStatusCode().value());
        CategoryResponseDto responseBody = (CategoryResponseDto) response.getBody();
        assertNotNull(responseBody);
        assertEquals(createdModel.getName(), responseBody.getName());
        assertEquals(createdModel.getDescription(), responseBody.getDescription());
        assertEquals(createdModel.getActive(), responseBody.getActive());
    }

    // --- Additional tests for uncovered methods ---

    @Test
    void updateCategory_shouldReturnUpdatedCategory() {
        UUID id = UUID.randomUUID();
        // Simulate a DTO for update
        CategoryUpdateDto updateDto =
                new CategoryUpdateDto("Actualizado", "Descripción actualizada", Boolean.TRUE, "test-user");
        CategoryModel updatedModel = CategoryModel.builder()
                .id(id)
                .name(updateDto.getName())
                .description(updateDto.getDescription())
                .active(updateDto.getActive())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        when(categoryService.update(eq(id), any(CategoryModel.class))).thenReturn(updatedModel);

        ResponseEntity<?> response = categoryController.updateCategory(id, updateDto);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Actualizado"));
    }

    @Test
    void getAllCategories_shouldReturnAll() {
        List<CategoryModel> models = List.of(
                CategoryModel.builder().name("A").description("desc").active(true).build(),
                CategoryModel.builder().name("B").description("desc").active(true).build()
        );
        when(categoryService.findAll()).thenReturn(models);

        ResponseEntity<?> response = categoryController.getAllCategories(null, null, "name", "asc");

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("A"));
        assertTrue(response.getBody().toString().contains("B"));
    }

    @Test
    void getAllCategories_shouldFilterAndSortByNameDesc() {
        CategoryModel c1 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Beta")
                .description("Test")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        CategoryModel c2 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Alpha")
                .description("Test")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryService.findAll()).thenReturn(List.of(c1, c2));

        ResponseEntity<?> response = categoryController.getAllCategories(null, true, "name", "desc");

        assertEquals(200, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("Beta", body.get(0).getName()); // should be first in descending
    }

    @Test
    void getAllCategories_shouldFilterByName() {
        CategoryModel match = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Utilities")
                .description("Something")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        CategoryModel nonMatch = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Finance")
                .description("Something else")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryService.findAll()).thenReturn(List.of(match, nonMatch));

        ResponseEntity<?> response = categoryController.getAllCategories("util", null, "name", "asc");

        assertEquals(200, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("Utilities", body.get(0).getName());
    }

    @Test
    void getAllCategories_shouldSortByCreatedAt() {
        CategoryModel c1 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("A")
                .description("Test")
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();
        CategoryModel c2 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("B")
                .description("Test")
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryService.findAll()).thenReturn(List.of(c1, c2));

        ResponseEntity<?> response = categoryController.getAllCategories(null, null, "create", "asc");

        assertEquals(200, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals("A", body.get(0).getName());
    }

    @Test
    void getAllCategories_shouldSortByUpdatedAtDesc() {
        CategoryModel c1 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("A")
                .updatedAt(LocalDateTime.now().minusHours(3))
                .build();
        CategoryModel c2 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("B")
                .updatedAt(LocalDateTime.now())
                .build();

        when(categoryService.findAll()).thenReturn(List.of(c1, c2));

        ResponseEntity<?> response = categoryController.getAllCategories(null, null, "update", "desc");

        assertEquals(200, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals("B", body.get(0).getName());
    }

    @Test
    void getAllCategories_shouldSortByActive() {
        CategoryModel c1 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Inactive")
                .active(false)
                .build();
        CategoryModel c2 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Active")
                .active(true)
                .build();

        when(categoryService.findAll()).thenReturn(List.of(c1, c2));

        ResponseEntity<?> response = categoryController.getAllCategories(null, null, "active", "asc");

        assertEquals(200, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals("Inactive", body.get(0).getName());
    }

    @Test
    void getAllCategories_shouldThrowOnInvalidSortBy() {
        when(categoryService.findAll()).thenReturn(List.of(categoryModel));

        assertThrows(IllegalArgumentException.class, () -> {
            categoryController.getAllCategories(null, null, "invalid", "asc");
        });
    }

    @Test
    void findById_shouldReturnCategory() {
        UUID id = UUID.randomUUID();
        when(categoryService.findById(id)).thenReturn(Optional.of(categoryModel));

        ResponseEntity<?> response = categoryController.findById(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteCategory_shouldDeleteSuccessfully() {
        UUID id = UUID.randomUUID();
        CategoryDeleteDto deleteDto =
                new CategoryDeleteDto(id);

        ResponseEntity<Void> response = categoryController.deleteCategory(deleteDto);

        verify(categoryService).permanentDelete(id);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void sortCategories_shouldReturnUnchangedList_whenInputIsNullOrEmpty() {
        // Case 1: Null input
        List<CategoryModel> resultWhenNull = invokeSortCategories(null, "name", "asc");
        assertNull(resultWhenNull);

        // Case 2: Empty input
        List<CategoryModel> resultWhenEmpty = invokeSortCategories(Collections.emptyList(), "name", "asc");
        assertTrue(resultWhenEmpty.isEmpty());
    }

    // Helper method to invoke private static method via reflection
    @SuppressWarnings("unchecked")
    private List<CategoryModel> invokeSortCategories(List<CategoryModel> input, String sortBy, String sortDirection) {
        try {
            java.lang.reflect.Method method = CategoryController.class.getDeclaredMethod("sortCategories", List.class, String.class, String.class);
            method.setAccessible(true);
            return (List<CategoryModel>) method.invoke(null, input, sortBy, sortDirection);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking sortCategories", e);
        }
    }

    @Test
    void getAllCategories_shouldFilterByNameAndSortByNameAsc() {
        String filterName = "test";
        CategoryModel cat1 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Test Alpha")
                .active(true)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        CategoryModel cat2 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Test Beta")
                .active(true)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        when(categoryService.findAll()).thenReturn(List.of(cat2, cat1));

        ResponseEntity<?> response = categoryController.getAllCategories(filterName, null, "name", "asc");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
    }

    @Test
    void getAllCategories_shouldFilterByActiveTrue() {
        CategoryModel activeCategory = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Activa")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        CategoryModel inactiveCategory = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Inactiva")
                .active(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryService.findAll()).thenReturn(List.of(activeCategory, inactiveCategory));

        ResponseEntity<?> response = categoryController.getAllCategories(null, true, "name", "asc");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("Activa", body.get(0).getName());
    }

    @Test
    void sortCategories_shouldThrowExceptionForUnsupportedSortBy() throws Exception {
        List<CategoryModel> categories = List.of(
                CategoryModel.builder().name("Alpha").build(),
                CategoryModel.builder().name("Beta").build()
        );

        String unsupportedSortBy = "invalid";
        String sortDirection = "asc";

        Method method = CategoryController.class.getDeclaredMethod("sortCategories", List.class, String.class, String.class);
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                () -> method.invoke(categoryController, categories, unsupportedSortBy, sortDirection)
        );

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertInstanceOf(IllegalArgumentException.class, cause);
        assertTrue(cause.getMessage().contains("Unsupported sortBy value: invalid"));
    }


}