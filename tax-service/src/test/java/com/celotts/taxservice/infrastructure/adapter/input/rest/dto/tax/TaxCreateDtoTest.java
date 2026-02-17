package com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaxCreateDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenDatesAreValid_ShouldNotHaveViolations() {
        TaxCreateDto dto = TaxCreateDto.builder()
                .code("TAX-001")
                .name("IVA General")
                .rate(new BigDecimal("16.00"))
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusDays(30)) // Fecha futura válida
                .createdBy("admin")
                .build();

        Set<ConstraintViolation<TaxCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
    }

    @Test
    void whenValidToIsBeforeValidFrom_ShouldFail() {
        TaxCreateDto dto = TaxCreateDto.builder()
                .code("TAX-002")
                .name("IVA Error")
                .rate(new BigDecimal("10.00"))
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().minusDays(1)) // Fecha pasada inválida
                .createdBy("admin")
                .build();

        Set<ConstraintViolation<TaxCreateDto>> violations = validator.validate(dto);
        
        assertFalse(violations.isEmpty());
        boolean hasDateError = violations.stream()
                .anyMatch(v -> v.getMessage().equals("{tax.date.range.invalid}"));
        
        assertTrue(hasDateError, "Debería fallar por rango de fechas inválido");
    }
}