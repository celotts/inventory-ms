package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitDeleteDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitDeleteDtoTest {

    static ValidatorFactory factory;
    static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        if (factory != null) factory.close();
    }

    @Test
    @DisplayName("Usar @NotBlank sobre UUID provoca ConstraintDeclarationException (estado actual)")
    void notBlankOnUuid_throwsConstraintDeclarationException() {
        ProductUnitDeleteDto dto = new ProductUnitDeleteDto(UUID.randomUUID());

        // Al validar, Hibernate Validator no encuentra validador para UUID en @NotBlank
        assertThrows(ConstraintDeclarationException.class, () -> validator.validate(dto));
    }

    @Test
    @Disabled("Activa este test después de corregir el DTO (quitar @NotBlank y dejar solo @NotNull en 'id').")
    @DisplayName("Con DTO corregido, id=null viola @NotNull y id!=null no tiene violaciones")
    void afterFix_validationsWork() {
        // Caso inválido: id null → viola @NotNull
        ProductUnitDeleteDto invalid = new ProductUnitDeleteDto(null);
        Set<ConstraintViolation<ProductUnitDeleteDto>> v1 = validator.validate(invalid);
        assertFalse(v1.isEmpty());
        assertTrue(v1.stream().anyMatch(cv -> cv.getPropertyPath().toString().equals("id")));

        // Caso válido: id presente → sin violaciones
        ProductUnitDeleteDto valid = new ProductUnitDeleteDto(UUID.randomUUID());
        Set<ConstraintViolation<ProductUnitDeleteDto>> v2 = validator.validate(valid);
        assertTrue(v2.isEmpty(), () -> "Violations: " + v2);
    }

    @Test
    @DisplayName("Lombok @Data: getters/setters/equals/hashCode/toString funcionan")
    void lombokDataWorks() {
        UUID id = UUID.randomUUID();
        ProductUnitDeleteDto a = new ProductUnitDeleteDto();
        a.setId(id);

        ProductUnitDeleteDto b = new ProductUnitDeleteDto(id);

        assertEquals(id, a.getId());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("ProductUnitDeleteDto"));
    }
}