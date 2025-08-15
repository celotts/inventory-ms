package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.port.category.usecase.CategoryUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryDeleteDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryStatusDto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryUseCase categoryUseCase;


    private UUID categoryId;
    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
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
        UUID id = categoryId;
        CategoryModel updated = categoryModel.toBuilder().active(false).build();

        when(categoryUseCase.updateStatus(id, false)).thenReturn(updated);

        ResponseEntity<?> response = categoryController.toggleCategoryStatus(id, false);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("\"active\":false")
                || response.getBody().toString().contains("false")); // depende de tu toString/DTO
    }

    @Test
    void toggleCategoryStatus_shouldReturnNotFound() {
        when(categoryUseCase.updateStatus(categoryId, false))
                .thenThrow(new IllegalArgumentException("Category not found"));

        assertThrows(IllegalArgumentException.class,
                () -> categoryController.toggleCategoryStatus(categoryId, false));
    }

    @Test
    void restoreCategory_shouldReturnRestoredCategory() {
        when(categoryUseCase.restore(categoryId)).thenReturn(categoryModel);

        ResponseEntity<?> response = categoryController.restoreCategory(categoryId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void getPaginatedCategories_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<CategoryModel> page = new PageImpl<>(List.of(categoryModel), pageable, 1);

        when(categoryUseCase.findAllPaginated(null, null, pageable)).thenReturn(page);

        ResponseEntity<Page<CategoryResponseDto>> response =
                categoryController.getPaginatedCategories(null, null, pageable);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getPaginatedCategories_shouldReturnPage2() {
        Pageable pageable = PageRequest.of(0, 10);

        CategoryModel model = CategoryModel.builder()
                .id(UUID.randomUUID()).name("Cat").active(true).build();

        Page<CategoryModel> page = new PageImpl<>(List.of(model), pageable, 1);

        when(categoryUseCase.findAllPaginated(null, null, pageable))
                .thenReturn(page);

        ResponseEntity<Page<CategoryResponseDto>> response =
                categoryController.getPaginatedCategories(null, null, pageable);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Cat", response.getBody().getContent().get(0).getName());
    }

    @Test
    void getCategoryStats_shouldReturnStats() {
        CategoryStatusDto stats = CategoryStatusDto.builder()
                .totalCategories(10).activeCategories(7).inactiveCategories(3).build();

        when(categoryUseCase.getCategoryStatistics()).thenReturn(stats);

        ResponseEntity<?> response = categoryController.getCategoryStats();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(stats, response.getBody());
    }

    @Test
    void existsByName_shouldReturnTrueOrFalse() {
        when(categoryUseCase.existsByName("Existing")).thenReturn(true);
        ResponseEntity<Boolean> exists = categoryController.categoryExists("Existing");
        assertTrue(exists.getBody());

        when(categoryUseCase.existsByName("Missing")).thenReturn(false);
        exists = categoryController.categoryExists("Missing");
        assertFalse(exists.getBody());
    }

    @Test
    void getActiveCategories_shouldReturnOnlyActive() {
        when(categoryUseCase.findAll()).thenReturn(List.of(categoryModel));

        ResponseEntity<?> response = categoryController.getActiveCategories();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    void searchCategories_shouldReturnMatching() {
        when(categoryUseCase.searchByNameOrDescription("Test", 10)).thenReturn(List.of(categoryModel));

        ResponseEntity<?> response = categoryController.searchCategories("Test", 10);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void permanentDeleteCategory_shouldCallService() {
        doNothing().when(categoryUseCase).permanentDelete(categoryId);

        ResponseEntity<Void> response = categoryController.permanentDeleteCategory(categoryId);

        verify(categoryUseCase).permanentDelete(categoryId);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() {
        CategoryCreateDto createDto = new CategoryCreateDto("Nueva Categoría", "Descripción");

        CategoryModel saved = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Nueva Categoría")
                .description("Descripción")
                .active(true)
                .build();
        lenient().when(categoryUseCase.create(any(CategoryModel.class))).thenReturn(saved);

        ResponseEntity<?> response = categoryController.createCategory(createDto);

        int statusCreate = response.getStatusCode().value();
        assertTrue(statusCreate == 201 || statusCreate == 200, "Expected 201 Created or 200 OK but was " + statusCreate);
        if (response.getBody() != null) {
            var body = (CategoryResponseDto) response.getBody();
            assertEquals("Nueva Categoría", body.getName());
            assertEquals("Descripción", body.getDescription());
        }
    }

    // --- Additional tests for uncovered methods ---

    @Test
    void updateCategory_shouldReturnUpdatedCategory() {
        UUID id = UUID.randomUUID();
        // Simulate a DTO for update
        com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto updateDto =
                new com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto("Actualizado", "Descripción actualizada", Boolean.TRUE, "test-user");

        CategoryModel existing = CategoryModel.builder()
                .id(id)
                .name("Old")
                .description("Old desc")
                .active(true)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        CategoryModel updatedModel = CategoryModel.builder()
                .id(id)
                .name(updateDto.getName())
                .description(updateDto.getDescription())
                .active(updateDto.getActive())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        lenient().when(categoryUseCase.update(eq(id), any(CategoryModel.class))).thenReturn(updatedModel);

        ResponseEntity<?> response = categoryController.updateCategory(id, updateDto);

        int statusUpdate = response.getStatusCode().value();
        assertTrue(statusUpdate == 200 || statusUpdate == 204, "Expected 200 OK or 204 No Content but was " + statusUpdate);
        if (response.getBody() != null) {
            assertTrue(response.getBody().toString().contains("Actualizado"));
        }
    }

    @Test
    void getAllCategories_shouldReturnAll() {
        List<CategoryModel> models = List.of(
                CategoryModel.builder().name("A").description("desc").active(true).build(),
                CategoryModel.builder().name("B").description("desc").active(true).build()
        );
        when(categoryUseCase.findAll()).thenReturn(models);
        List<CategoryModel> list = List.of(
                CategoryModel.builder().id(UUID.randomUUID()).name("Alpha").active(true).build(),
                CategoryModel.builder().id(UUID.randomUUID()).name("Beta").active(false).build()
        );
        when(categoryUseCase.findAll()).thenReturn(list);

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

        when(categoryUseCase.findAll()).thenReturn(List.of(c1, c2));

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

        when(categoryUseCase.findAll()).thenReturn(List.of(match, nonMatch));

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

        when(categoryUseCase.findAll()).thenReturn(List.of(c1, c2));

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

        when(categoryUseCase.findAll()).thenReturn(List.of(c1, c2));

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

        when(categoryUseCase.findAll()).thenReturn(List.of(c1, c2));

        ResponseEntity<?> response = categoryController.getAllCategories(null, null, "active", "asc");

        assertEquals(200, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        List<CategoryResponseDto> body = (List<CategoryResponseDto>) response.getBody();
        assertNotNull(body);
        assertEquals("Inactive", body.get(0).getName());
    }

    @Test
    void getAllCategories_shouldThrowOnInvalidSortBy() {
        //when(categoryUseCase.findAll()).thenReturn(List.of(categoryModel));
        when(categoryUseCase.findAll()).thenReturn(List.of(
                CategoryModel.builder().id(UUID.randomUUID()).name("X").build()
        ));
        assertThrows(IllegalArgumentException.class, () -> {
            categoryController.getAllCategories(null, null, "invalid", "asc");
        });
    }

    @Test
    void findById_shouldReturnCategory() {
        when(categoryUseCase.findById(categoryId)).thenReturn(Optional.of(categoryModel));

        ResponseEntity<?> response = categoryController.findById(categoryId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteCategory_shouldDeleteSuccessfully() {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryUseCase).permanentDelete(id);

        var deleteDto = new CategoryDeleteDto(id);

        ResponseEntity<Void> response = categoryController.deleteCategory(deleteDto);

        verify(categoryUseCase).permanentDelete(id);
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

        when(categoryUseCase.findAll()).thenReturn(List.of(cat2, cat1));

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

        when(categoryUseCase.findAll()).thenReturn(List.of(activeCategory, inactiveCategory));

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