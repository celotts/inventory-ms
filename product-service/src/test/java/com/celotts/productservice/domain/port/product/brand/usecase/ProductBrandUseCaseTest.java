package com.celotts.productservice.domain.port.product.brand.usecase;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductBrandUseCaseTest {

    private ProductBrandUseCase useCase;
    private final UUID brandId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Creamos un spy sobre la interfaz para probar métodos default
        useCase = Mockito.spy(ProductBrandUseCase.class);
    }

    @Test
    void create_shouldCallSaveAndReturnSavedModel() {
        ProductBrandModel brand = ProductBrandModel.builder()
                .name("Marca X")
                .enabled(true)
                .build();

        ProductBrandModel saved = ProductBrandModel.builder()
                .id(brandId)
                .name("Marca X")
                .enabled(true)
                .build();

        // Simulamos que save() devuelve un modelo con ID
        doReturn(saved).when(useCase).save(brand);

        ProductBrandModel result = useCase.create(brand);

        // Verificamos delegación
        verify(useCase).save(brand);
        assertEquals(brandId, result.getId());
        assertEquals("Marca X", result.getName());
    }

    @Test
    void update_shouldInjectIdAndCallSave() {
        ProductBrandModel brandToUpdate = ProductBrandModel.builder()
                .name("Marca Editada")
                .enabled(false)
                .build();

        ProductBrandModel updated = ProductBrandModel.builder()
                .id(brandId)
                .name("Marca Editada")
                .enabled(false)
                .build();

        doReturn(updated).when(useCase).save(any(ProductBrandModel.class));

        ProductBrandModel result = useCase.update(brandId, brandToUpdate);

        // Validación: el ID fue inyectado y save() fue invocado
        verify(useCase).save(brandToUpdate);
        assertEquals(brandId, result.getId());
        assertEquals("Marca Editada", result.getName());
    }

    @Test
    void delete_shouldCallDeleteById() {
        doNothing().when(useCase).deleteById(brandId);

        useCase.delete(brandId);

        verify(useCase).deleteById(brandId);
    }
}