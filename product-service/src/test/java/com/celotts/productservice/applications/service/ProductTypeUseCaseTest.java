package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.ProductTypeUseCaseImpl;
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
class ProductTypeUseCaseTest {

    // 👇 Mock correcto: el puerto de salida (ajusta el nombre del paquete/clase si difiere)
    @Mock
    private com.celotts.productservice.domain.port.product.type.output.ProductTypeRepositoryPort repo;

    // SUT
    private ProductTypeUseCaseImpl useCase;

    private final String code = "FOOD";
    private ProductTypeEntity productTypeEntity;

    @BeforeEach
    void setUp() {
        useCase = new ProductTypeUseCaseImpl(repo);   // 👈 pasa el repo, no el use case

        productTypeEntity = ProductTypeEntity.builder()
                .code("FOOD")
                .name("Food")
                .enabled(true)
                .build();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void existsByCode_shouldReturnTrue_whenExists() {
        when(repo.existsByCode(code)).thenReturn(true);

        boolean result = useCase.existsByCode(code);

        assertTrue(result);
        verify(repo).existsByCode(code);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findNameByCode_shouldReturnName_whenExists() {
        when(repo.findNameByCode(code)).thenReturn(Optional.of("Food"));

        Optional<String> result = useCase.findNameByCode(code);

        assertTrue(result.isPresent());
        assertEquals("Food", result.get());
        verify(repo).findNameByCode(code);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findAllCodes_shouldReturnListOfCodes() {
        List<String> codes = List.of("FOOD", "DRINK");
        when(repo.findAllCodes()).thenReturn(codes);

        List<String> result = useCase.findAllCodes();

        assertEquals(2, result.size());
        assertEquals("FOOD", result.get(0));
        verify(repo).findAllCodes();
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findByCode_shouldReturnEntity_whenExists() {
        when(repo.findByCode(code)).thenReturn(Optional.of(productTypeEntity));

        Optional<ProductTypeEntity> result = useCase.findByCode(code);

        assertTrue(result.isPresent());
        assertEquals("Food", result.get().getName());
        verify(repo).findByCode(code);
    }

    @Test
    @WithMockUser(username = "tester", roles = {"ADMIN"})
    void findByCode_shouldReturnEmpty_whenNotExists() {
        when(repo.findByCode(code)).thenReturn(Optional.empty());

        Optional<ProductTypeEntity> result = useCase.findByCode(code);

        assertTrue(result.isEmpty());
        verify(repo).findByCode(code);
    }
}