package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBrandControllerNotFoundTest {

    @Mock ProductBrandUseCase productBrandUseCase;
    @Mock ProductBrandDtoMapper productBrandDtoMapper;
    @Mock ProductBrandService productBrandService;

    @InjectMocks ProductBrandController controller;

    @Test
    void getBrandNameById_shouldThrow404_whenServiceReturnsEmpty() {
        UUID id = UUID.randomUUID();
        when(productBrandService.findNameById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> controller.getBrandNameById(id)  // <-- ejecuta el lambda del orElseThrow
        );

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Brand name not found", ex.getReason());
        verify(productBrandService).findNameById(id);
        verifyNoInteractions(productBrandUseCase, productBrandDtoMapper);
    }
}