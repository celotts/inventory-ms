package com.celotts.productservice.infrastructure.common.error;

import com.celotts.productserviceOld.infrastructure.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    @Test
    void debe_contener_todos_los_codigos_esperados_en_orden() {
        List<ErrorCode> expected = List.of(
                ErrorCode.VALIDATION_ERROR,
                ErrorCode.RESOURCE_NOT_FOUND,
                ErrorCode.RESOURCE_ALREADY_EXISTS,
                ErrorCode.BAD_REQUEST,
                ErrorCode.UNAUTHORIZED,
                ErrorCode.FORBIDDEN,
                ErrorCode.CONFLICT,
                ErrorCode.INTERNAL_ERROR,
                ErrorCode.DATABASE_ERROR,
                ErrorCode.INTEGRATION_ERROR
        );

        // Verifica tamaño exacto
        assertThat(ErrorCode.values()).hasSize(expected.size());

        // Verifica que el orden y contenido coincidan
        assertThat(List.of(ErrorCode.values())).containsExactlyElementsOf(expected);

        // Verifica que el EnumSet incluya exactamente estos elementos (sin duplicados)
        assertThat(EnumSet.allOf(ErrorCode.class)).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void valueOf_debe_resolver_cada_nombre_correctamente() {
        for (ErrorCode code : ErrorCode.values()) {
            String name = code.name();
            assertThat(ErrorCode.valueOf(name)).isSameAs(code);
        }
    }

    @Test
    void los_nombres_deben_ser_unicos_y_en_mayusculas_con_guiones_bajos() {
        List<String> names = List.of(ErrorCode.values())
                .stream().map(Enum::name).collect(Collectors.toList());

        // Unicidad
        assertThat(names).doesNotHaveDuplicates();

        // Formato básico (MAYUSCULAS y _)
        assertThat(names).allMatch(n -> n.matches("[A-Z][A-Z0-9_]*"));
    }
}