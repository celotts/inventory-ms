package com.celotts.productservice.infrastructure.common.error;

import com.celotts.productserviceOld.infrastructure.common.error.ApiErrors;
import com.celotts.productserviceOld.infrastructure.common.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiErrorsTest {

    @Test
    void problem_debe_construir_ProblemDetail_con_campos_basicos_y_extras() {
        // given
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        String title = "Datos inválidos";
        String detail = "El campo 'code' es obligatorio";
        Map<String,Object> extras = Map.of(
                "field", "code",
                "attempt", 3
        );

        // when
        ProblemDetail pd = ApiErrors.problem(status, code, title, detail, extras);

        // then: básicos
        assertThat(pd).isNotNull();
        assertThat(pd.getStatus()).isEqualTo(status.value());
        assertThat(pd.getTitle()).isEqualTo(title);
        assertThat(pd.getDetail()).isEqualTo(detail);

        // type y errorCode
        assertThat(pd.getType()).isEqualTo(URI.create("urn:celotts:error:" + code.name().toLowerCase()));
        assertThat(pd.getProperties())
                .containsEntry("errorCode", code.name());

        // timestamp presente y de tipo OffsetDateTime
        assertThat(pd.getProperties())
                .containsKey("timestamp");
        assertThat(pd.getProperties().get("timestamp"))
                .isInstanceOf(OffsetDateTime.class);

        // extras mergeados
        assertThat(pd.getProperties())
                .containsEntry("field", "code")
                .containsEntry("attempt", 3);
    }

    @Test
    void problem_debe_funcionar_cuando_extras_es_null() {
        // given
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorCode code = ErrorCode.RESOURCE_NOT_FOUND;
        String title = "No encontrado";
        String detail = "Producto no encontrado";

        // when
        ProblemDetail pd = ApiErrors.problem(status, code, title, detail, null);

        // then
        assertThat(pd.getStatus()).isEqualTo(status.value());
        assertThat(pd.getTitle()).isEqualTo(title);
        assertThat(pd.getDetail()).isEqualTo(detail);
        assertThat(pd.getType()).isEqualTo(URI.create("urn:celotts:error:" + code.name().toLowerCase()));

        // propiedades mínimas
        assertThat(pd.getProperties())
                .containsKeys("errorCode", "timestamp");
        assertThat(pd.getProperties().get("errorCode")).isEqualTo(code.name());
        assertThat(pd.getProperties().get("timestamp")).isInstanceOf(OffsetDateTime.class);
    }
}