package com.celotts.productservice.application.usecase;

import com.celotts.productservice.application.usecase.product.ProductTypeUseCaseImpl;
import com.celotts.productservice.domain.model.ProductTypeModel;
import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductTypeUseCaseImplTest {

    private ProductTypeRepositoryPort repository;
    private ProductTypeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ProductTypeRepositoryPort.class);
        useCase = new ProductTypeUseCaseImpl(repository);
    }

    @Test
    void existsByCode_true_whenRepoSaysTrue() {
        when(repository.existsByCode("FOOD")).thenReturn(true);
        assertTrue(useCase.existsByCode("FOOD"));
        verify(repository).existsByCode("FOOD");
    }

    @Test
    void existsByCode_false_whenRepoSaysFalse() {
        when(repository.existsByCode("UNKNOWN")).thenReturn(false);
        assertFalse(useCase.existsByCode("UNKNOWN"));
        verify(repository).existsByCode("UNKNOWN");
    }

    @Test
    void findNameByCode_found_returnsOptionalWithValue() {
        when(repository.findNameByCode("FOOD")).thenReturn(Optional.of("Food"));
        assertEquals(Optional.of("Food"), useCase.findNameByCode("FOOD"));
        verify(repository).findNameByCode("FOOD");
    }

    @Test
    void findNameByCode_notFound_returnsEmpty() {
        when(repository.findNameByCode("X")).thenReturn(Optional.empty());
        assertTrue(useCase.findNameByCode("X").isEmpty());
        verify(repository).findNameByCode("X");
    }

    @Test
    void findAllCodes_delegatesAndReturnsList() {
        var expected = List.of("FOOD", "BEV", "TOOL");
        when(repository.findAllCodes()).thenReturn(expected);
        assertEquals(expected, useCase.findAllCodes());
        verify(repository).findAllCodes();
    }

    @Test
    void findByCode_found_returnsModel() {
        var model = ProductTypeModel.builder().code("FOOD").name("Food").build();
        when(repository.findByCode("FOOD")).thenReturn(Optional.of(model));
        assertEquals(Optional.of(model), useCase.findByCode("FOOD"));
        verify(repository).findByCode("FOOD");
    }

    @Test
    void findByCode_notFound_returnsEmpty() {
        when(repository.findByCode("NOPE")).thenReturn(Optional.empty());
        assertTrue(useCase.findByCode("NOPE").isEmpty());
        verify(repository).findByCode("NOPE");
    }

    @Test
    void postConstruct_log_printsMessage() {
        // Captura de System.out para cubrir el método @PostConstruct (log)
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            useCase.log(); // invocamos directamente el método marcado con @PostConstruct
            String out = baos.toString();
            assertTrue(out.contains("ProductTypeUseCaseImpl fue cargado"));
        } finally {
            System.setOut(original);
        }
    }
}