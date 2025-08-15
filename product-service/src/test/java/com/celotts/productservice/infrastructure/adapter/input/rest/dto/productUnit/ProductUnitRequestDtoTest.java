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

class ProductUnitRequestDtoTest {

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

    private boolean hasViolation(Set<ConstraintViolation<ProductUnitRequestDto>> violations,
                                 String property,
                                 String messageContains) {
        return violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals(property)
                        && v.getMessage().contains(messageContains)
        );
    }

    @Test
    @DisplayName("Nombre válido en límites (2 y 100) no viola restricciones")
    void nameBounds_valid() {
        ProductUnitRequestDto dtoMin = new ProductUnitRequestDto();
        dtoMin.setName("Ok"); // 2 chars
        Set<ConstraintViolation<ProductUnitRequestDto>> v1 = validator.validate(dtoMin);
        assertTrue(v1.isEmpty(), () -> "Violations: " + v1);

        ProductUnitRequestDto dtoMax = new ProductUnitRequestDto();
        dtoMax.setName("x".repeat(100));
        Set<ConstraintViolation<ProductUnitRequestDto>> v2 = validator.validate(dtoMax);
        assertTrue(v2.isEmpty(), () -> "Violations: " + v2);
    }

    @Test
    @DisplayName("Nombre demasiado corto o demasiado largo viola @Size con mensaje esperado")
    void nameBounds_invalidBySize() {
        ProductUnitRequestDto dtoShort = new ProductUnitRequestDto();
        dtoShort.setName("A"); // 1 char
        Set<ConstraintViolation<ProductUnitRequestDto>> vShort = validator.validate(dtoShort);
        assertFalse(vShort.isEmpty());
        assertTrue(hasViolation(vShort, "name", "between 2 and 100 characters"));

        ProductUnitRequestDto dtoLong = new ProductUnitRequestDto();
        dtoLong.setName("x".repeat(101));
        Set<ConstraintViolation<ProductUnitRequestDto>> vLong = validator.validate(dtoLong);
        assertFalse(vLong.isEmpty());
        assertTrue(hasViolation(vLong, "name", "between 2 and 100 characters"));
    }

    @Test
    @DisplayName("Nombre con caracteres inválidos viola @Pattern con mensaje esperado")
    void name_invalidByPattern() {
        ProductUnitRequestDto dto = new ProductUnitRequestDto();
        dto.setName("Bad@Name"); // '@' no permitido
        Set<ConstraintViolation<ProductUnitRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(hasViolation(violations, "name", "contains invalid characters"));
    }

    @Test
    @DisplayName("Descripción de 500 OK, 501 viola @Size con mensaje esperado")
    void description_maxSize() {
        ProductUnitRequestDto ok = new ProductUnitRequestDto();
        ok.setName("ValidName");
        ok.setDescription("y".repeat(500));
        Set<ConstraintViolation<ProductUnitRequestDto>> vOk = validator.validate(ok);
        assertTrue(vOk.isEmpty(), () -> "Violations: " + vOk);

        ProductUnitRequestDto bad = new ProductUnitRequestDto();
        bad.setName("ValidName");
        bad.setDescription("z".repeat(501));
        Set<ConstraintViolation<ProductUnitRequestDto>> vBad = validator.validate(bad);
        assertFalse(vBad.isEmpty());
        assertTrue(hasViolation(vBad, "description", "must not exceed 500 characters"));
    }

    @Test
    @DisplayName("enabled por defecto es true y no tiene validaciones Bean Validation")
    void enabled_defaultTrue() {
        ProductUnitRequestDto dto = new ProductUnitRequestDto();
        assertEquals(Boolean.TRUE, dto.getEnabled());

        // No hay anotaciones de validación sobre enabled, null no genera violación
        dto.setEnabled(null);
        Set<ConstraintViolation<ProductUnitRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), () -> "Violations: " + violations);
    }

    @Test
    @DisplayName("createdBy/updatedBy son opcionales (sin violaciones)")
    void createdBy_updatedBy_optional() {
        ProductUnitRequestDto dto = new ProductUnitRequestDto();
        dto.setName("NombreValido");
        dto.setDescription("desc");
        dto.setCreatedBy(null);
        dto.setUpdatedBy(null);
        Set<ConstraintViolation<ProductUnitRequestDto>> v = validator.validate(dto);
        assertTrue(v.isEmpty(), () -> "Violations: " + v);
    }

    @Test
    @DisplayName("@Data (equals/hashCode/toString/getters/setters) funciona")
    void dataMethods() {
        ProductUnitRequestDto a = new ProductUnitRequestDto();
        a.setName("Name");
        a.setDescription("Desc");
        a.setEnabled(true);
        a.setCreatedBy("c");
        a.setUpdatedBy("u");

        ProductUnitRequestDto b = new ProductUnitRequestDto();
        b.setName("Name");
        b.setDescription("Desc");
        b.setEnabled(true);
        b.setCreatedBy("c");
        b.setUpdatedBy("u");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("ProductUnitRequestDto"));

        b.setName("Name2");
        assertEquals("Name2", b.getName());
    }

    @Test
    @DisplayName("name = null es válido (Pattern y Size permiten null)")
    void name_null_isValid() {
        ProductUnitRequestDto dto = new ProductUnitRequestDto();
        dto.setName(null);
        Set<ConstraintViolation<ProductUnitRequestDto>> v = validator.validate(dto);
        assertTrue(v.isEmpty(), () -> "Violations: " + v);
    }
}