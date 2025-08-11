package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.port.product.type.usecase.ProductTypeUseCase;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceTest {

    @Mock
    private ProductTypeUseCase productTypeUseCase;

    private ProductTypeService productTypeService;

    private final String code = "FOOD";

    private ProductTypeEntity productTypeEntity;

    @BeforeEach
    void setUp() {
        productTypeService = new ProductTypeService(productTypeUseCase);

        productTypeEntity = ProductTypeEntity.builder()
                .code("FOOD")
                .name("Food")
                .enabled(true)
                .build();
    }

    @Test 
@WithMockUser(username = "tester", roles = {"ADMIN"})
    
    void existsByCode_shouldReturnTrue_whenExists() {
        when(productTypeUseCase.existsByCode(code)).thenReturn(true);

        boolean result = productTypeService.existsByCode(code);

        assertTrue(result);
        verify(productTypeUseCase).existsByCode(code);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findNameByCode_shouldReturnName_whenExists() {
        when(productTypeUseCase.findNameByCode(code)).thenReturn(Optional.of("Food"));

        Optional<String> result = productTypeService.findNameByCode(code);

        assertTrue(result.isPresent());
        assertEquals("Food", result.get());
        verify(productTypeUseCase).findNameByCode(code);
    }

    @Test 
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findAllCodes_shouldReturnListOfCodes() {
        List<String> codes = List.of("FOOD", "DRINK");
        when(productTypeUseCase.findAllCodes()).thenReturn(codes);

        List<String> result = productTypeService.findAllCodes();

        assertEquals(2, result.size());
        assertEquals("FOOD", result.get(0));
        verify(productTypeUseCase).findAllCodes();
    }

    @Test 
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findByCode_shouldReturnEntity_whenExists() {
        when(productTypeUseCase.findByCode(code)).thenReturn(Optional.of(productTypeEntity));

        Optional<ProductTypeEntity> result = productTypeService.findByCode(code);

        assertTrue(result.isPresent());
        assertEquals("Food", result.get().getName());
        verify(productTypeUseCase).findByCode(code);
    }

    @Test 
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findByCode_shouldReturnEmpty_whenNotExists() {
        when(productTypeUseCase.findByCode(code)).thenReturn(Optional.empty());

        Optional<ProductTypeEntity> result = productTypeService.findByCode(code);

        assertTrue(result.isEmpty());
        verify(productTypeUseCase).findByCode(code);
    }
}