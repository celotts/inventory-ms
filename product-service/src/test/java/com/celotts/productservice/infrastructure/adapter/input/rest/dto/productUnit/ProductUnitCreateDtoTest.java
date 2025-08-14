package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
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

class ProductUnitCreateDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        if (factory != null) factory.close();
    }

    private boolean hasViolation(Set<ConstraintViolation<ProductUnitCreateDto>> violations,
                                 String property,
                                 String messageContains) {
        return violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals(property)
                        && v.getMessage().contains(messageContains)
        );
    }

    @Test
    @DisplayName("DTO válido no produce violaciones")
    void validDto_hasNoViolations() {
        ProductUnitCreateDto dto = ProductUnitCreateDto.builder()
                .code("KG")
                .name("Kilogram")
                .description("Metric mass unit")
                .enabled(true)
                .symbol("kg")
                .createdBy("tester")
                .updatedBy("tester2")
                .build();

        Set<ConstraintViolation<ProductUnitCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), () -> "Violaciones inesperadas: " + violations);
    }

    @Test
    @DisplayName("Campos en blanco y enabled=null generan violaciones con mensajes esperados")
    void blankFields_andNullEnabled_haveViolations() {
        ProductUnitCreateDto dto = ProductUnitCreateDto.builder()
                .code(" ")
                .name(" ")
                .description(" ")
                .enabled(null)
                .symbol(" ")
                .createdBy(" ")
                .updatedBy(null)
                .build();

        Set<ConstraintViolation<ProductUnitCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        // NotBlank (sin mensaje custom en code)
        assertTrue(hasViolation(violations, "code", "must not be blank")
                || hasViolation(violations, "code", "no debe estar vacío"));

        // NotBlank con mensajes custom
        assertTrue(hasViolation(violations, "name", "Name is required"));
        assertTrue(hasViolation(violations, "description", "Description is required"));
        assertTrue(hasViolation(violations, "symbol", "Symbol is required"));
        assertTrue(hasViolation(violations, "createdBy", "createdBy is required"));

        // NotNull con mensaje custom
        assertTrue(hasViolation(violations, "enabled", "Enabled flag is required"));
    }

    @Test
    @DisplayName("Restricciones de longitud en name (100) y description (500) se aplican")
    void sizeConstraints_forName_andDescription() {
        String over100 = "x".repeat(101);
        String over500 = "y".repeat(501);

        ProductUnitCreateDto dto = ProductUnitCreateDto.builder()
                .code("UN")
                .name(over100)
                .description(over500)
                .enabled(true)
                .symbol("u")
                .createdBy("tester")
                .build();

        Set<ConstraintViolation<ProductUnitCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        assertTrue(hasViolation(violations, "name", "must not exceed 100 characters"));
        assertTrue(hasViolation(violations, "description", "must not exceed 500 characters"));
    }

    @Test
    @DisplayName("@Builder y @Data funcionan (equals/hashCode/toString/getters/setters)")
    void builder_and_dataMethods() {
        ProductUnitCreateDto a = ProductUnitCreateDto.builder()
                .code("LT")
                .name("Liter")
                .description("Volume unit")
                .enabled(false)
                .symbol("L")
                .createdBy("user1")
                .updatedBy("user2")
                .build();

        ProductUnitCreateDto b = ProductUnitCreateDto.builder()
                .code("LT")
                .name("Liter")
                .description("Volume unit")
                .enabled(false)
                .symbol("L")
                .createdBy("user1")
                .updatedBy("user2")
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("ProductUnitCreateDto"));

        // setters/getters
        b.setName("Liter (US)");
        assertEquals("Liter (US)", b.getName());
    }

    @Test
    @DisplayName("Builder.toString() cubre ProductUnitCreateDtoBuilder")
    void builder_toString_isNotBlank() {
        String s = ProductUnitCreateDto.builder()
                .code("KG")
                .name("Kilogram")
                .description("Metric mass unit")
                .enabled(true)
                .symbol("kg")
                .createdBy("tester")
                .updatedBy("tester2")
                .toString(); // <- cubre el toString() del builder

        assertNotNull(s);
        assertFalse(s.isBlank());
        // opcional: muchos Lombok incluyen el nombre de la clase interna
        assertTrue(s.contains("ProductUnitCreateDtoBuilder"));
    }
}