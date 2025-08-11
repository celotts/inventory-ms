package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitUpdateDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        if (factory != null) factory.close();
    }

    private static boolean hasViolation(Set<ConstraintViolation<ProductUnitUpdateDto>> violations,
                                        String property, String contains) {
        return violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals(property)
                        && v.getMessage().contains(contains));
    }

    @Test
    @DisplayName("DTO válido no produce violaciones")
    void validDto_hasNoViolations() {
        ProductUnitUpdateDto dto = ProductUnitUpdateDto.builder()
                .name("Kilogram")
                .description("Metric mass unit")
                .symbol("kg")
                .enabled(true)
                .updatedBy("tester")
                .build();

        Set<ConstraintViolation<ProductUnitUpdateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), () -> "Violations: " + violations);
    }

    @Test
    @DisplayName("Campos en blanco y enabled=null generan violaciones con mensajes esperados")
    void blankFields_andNullEnabled_haveViolations() {
        ProductUnitUpdateDto dto = ProductUnitUpdateDto.builder()
                .name(" ")
                .description(" ")
                .symbol(" ")
                .enabled(null)
                .updatedBy(" ")
                .build();

        Set<ConstraintViolation<ProductUnitUpdateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        assertTrue(hasViolation(violations, "name", "Name is required"));
        assertTrue(hasViolation(violations, "description", "Description is required"));
        assertTrue(hasViolation(violations, "symbol", "Symbol is required"));
        assertTrue(hasViolation(violations, "updatedBy", "updatedBy is required"));
        assertTrue(hasViolation(violations, "enabled", "Enabled flag is required"));
    }

    @Test
    @DisplayName("Restricciones de longitud: name<=100 y description<=500")
    void sizeConstraints() {
        String over100 = "x".repeat(101);
        String over500 = "y".repeat(501);

        ProductUnitUpdateDto dto = ProductUnitUpdateDto.builder()
                .name(over100)
                .description(over500)
                .symbol("kg")
                .enabled(true)
                .updatedBy("user")
                .build();

        Set<ConstraintViolation<ProductUnitUpdateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "name", "must not exceed 100 characters"));
        assertTrue(hasViolation(violations, "description", "must not exceed 500 characters"));
    }

    @Test
    @DisplayName("@Builder/@Data: equals, hashCode, toString y getters/setters")
    void builder_and_dataMethods() {
        ProductUnitUpdateDto a = ProductUnitUpdateDto.builder()
                .name("Liter")
                .description("Volume unit")
                .symbol("L")
                .enabled(false)
                .updatedBy("user1")
                .build();

        ProductUnitUpdateDto b = ProductUnitUpdateDto.builder()
                .name("Liter")
                .description("Volume unit")
                .symbol("L")
                .enabled(false)
                .updatedBy("user1")
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("ProductUnitUpdateDto"));

        b.setSymbol("LT");
        assertEquals("LT", b.getSymbol());
    }

    @Test
    @DisplayName("Builder.toString() se ejecuta (cubre clase interna del builder)")
    void builder_toString_isNotBlank() {
        String s = ProductUnitUpdateDto.builder()
                .name("Unit")
                .description("Any")
                .symbol("U")
                .enabled(true)
                .updatedBy("tester")
                .toString(); // <-- cubre el toString() del builder

        assertNotNull(s);
        assertFalse(s.isBlank());
        // opcional, suele incluir el nombre de la clase interna generada por Lombok
        assertTrue(s.contains("ProductUnitUpdateDtoBuilder"));
    }
}