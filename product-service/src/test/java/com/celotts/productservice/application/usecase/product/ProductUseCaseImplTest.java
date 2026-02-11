package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;
    @Mock
    private ProductUnitRepositoryPort productUnitPort;
    @Mock
    private ProductBrandRepositoryPort productBrandPort;
    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

    private ProductModel product;
    private UUID productId;
    private UUID brandId;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        brandId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        product = ProductModel.builder()
                .id(productId)
                .code("TEST-001")
                .name("Test Product")
                .enabled(true)
                .unitCode("UNIT")
                .brandId(brandId)
                .categoryId(categoryId)
                .build();
    }

    // --- Tests for createProduct ---

    @Test
    @DisplayName("createProduct should throw exception when code exists")
    void createProduct_whenCodeExists_shouldThrowException() {
        // Given
        when(productRepositoryPort.findByCode("TEST-001")).thenReturn(Optional.of(product));

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            productUseCase.createProduct(product);
        });
        verify(productRepositoryPort, never()).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("createProduct should save product when code is new")
    void createProduct_whenCodeIsNew_shouldSaveProduct() {
        // Given
        when(productRepositoryPort.findByCode("TEST-001")).thenReturn(Optional.empty());
        when(productRepositoryPort.save(any(ProductModel.class))).thenReturn(product);
        mockValidationDependencies(true); // Mock dependencies since they will be called

        // When
        ProductModel result = productUseCase.createProduct(product);

        // Then
        assertNotNull(result);
        assertEquals("TEST-001", result.getCode());
        verify(productRepositoryPort, times(1)).save(product);
    }

    // --- Tests for updateProduct ---

    @Test
    @DisplayName("updateProduct should throw exception when product not found")
    void updateProduct_whenProductNotFound_shouldThrowException() {
        // Given
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productUseCase.updateProduct(productId, product);
        });
        verify(productRepositoryPort, never()).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("updateProduct should update and save product when found")
    void updateProduct_whenProductFound_shouldUpdateAndSave() {
        // Given
        ProductModel updates = ProductModel.builder().name("Updated Name").build();
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // No need to mock validation dependencies here if 'updates' has null references

        // When
        ProductModel result = productUseCase.updateProduct(productId, updates);

        // Then
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("TEST-001", result.getCode()); // Should not change
        verify(productRepositoryPort, times(1)).save(any(ProductModel.class));
    }

    // --- Tests for getProductById ---

    @Test
    @DisplayName("getProductById should throw exception when not found")
    void getProductById_whenNotFound_shouldThrowException() {
        // Given
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productUseCase.getProductById(productId);
        });
    }

    @Test
    @DisplayName("getProductById should return product when found")
    void getProductById_whenFound_shouldReturnProduct() {
        // Given
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));

        // When
        ProductModel result = productUseCase.getProductById(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getId());
    }

    // --- Tests for disableProduct ---

    @Test
    @DisplayName("disableProduct should set enabled to false and save")
    void disableProduct_shouldSetEnabledFalseAndSave() {
        // Given
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        productUseCase.disableProduct(productId);

        // Then
        verify(productRepositoryPort, times(1)).save(argThat(savedProduct -> !savedProduct.getEnabled()));
    }

    // --- Tests for enableProduct ---

    @Test
    @DisplayName("enableProduct should set enabled to true and save")
    void enableProduct_shouldSetEnabledTrueAndSave() {
        // Given
        product.setEnabled(false); // Start with a disabled product
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProductModel result = productUseCase.enableProduct(productId);

        // Then
        assertTrue(result.getEnabled());
        verify(productRepositoryPort, times(1)).save(any(ProductModel.class));
    }

    @Test
    @DisplayName("enableProduct should do nothing if already enabled")
    void enableProduct_whenAlreadyEnabled_shouldNotSave() {
        // Given
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product)); // product is enabled by default

        // When
        ProductModel result = productUseCase.enableProduct(productId);

        // Then
        assertTrue(result.getEnabled());
        verify(productRepositoryPort, never()).save(any(ProductModel.class));
    }

    private void mockValidationDependencies(boolean shouldExist) {
        when(productUnitPort.existsByCode(anyString())).thenReturn(shouldExist);
        when(productBrandPort.existsById(any(UUID.class))).thenReturn(shouldExist);
        when(categoryRepositoryPort.existsById(any(UUID.class))).thenReturn(shouldExist);
    }
}
