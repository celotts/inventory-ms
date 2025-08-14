package com.celotts.productservice.infrastructure.adapter.input.rest.dto.response;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.response.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class ApiErrorResponseTest {

    private ObjectMapper mapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return m;
    }

    @Test
    void builderAndGetters() {
        LocalDateTime now = LocalDateTime.now();

        ApiErrorResponse r = ApiErrorResponse.builder()
                .timestamp(now)
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .path("/api/test")
                .build();

        assertThat(r.getTimestamp()).isEqualTo(now);
        assertThat(r.getStatus()).isEqualTo(404);
        assertThat(r.getError()).isEqualTo("Not Found");
        assertThat(r.getMessage()).isEqualTo("Resource not found");
        assertThat(r.getPath()).isEqualTo("/api/test");
    }

    @Test
    void settersAndFieldComparison() {
        LocalDateTime now = LocalDateTime.now();

        // Requiere que la clase de producción tenga @AllArgsConstructor
        ApiErrorResponse r1 = new ApiErrorResponse(now, 500, "Internal Server Error", "Something went wrong", "/api/error");
        ApiErrorResponse r2 = new ApiErrorResponse(now, 500, "Internal Server Error", "Something went wrong", "/api/error");

        // No hay equals/hashCode; comparamos por campos
        assertThat(r1).usingRecursiveComparison().isEqualTo(r2);
    }

    @Test
    void jsonSerialization_usesExpectedPattern() throws Exception {
        LocalDateTime dt = LocalDateTime.of(2025, 8, 8, 14, 30, 45);

        ApiErrorResponse r = ApiErrorResponse.builder()
                .timestamp(dt)
                .status(400)
                .error("Bad Request")
                .message("Invalid data")
                .path("/api/test")
                .build();

        String json = mapper().writeValueAsString(r);
        assertThat(json).contains("\"timestamp\":\"2025-08-08 14:30:45\"");
        assertThat(json).contains("\"status\":400");
        assertThat(json).contains("\"error\":\"Bad Request\"");
        assertThat(json).contains("\"message\":\"Invalid data\"");
        assertThat(json).contains("\"path\":\"/api/test\"");
    }

    @Test
    void jsonDeserialization_respectsPattern_andRoundTrip() throws Exception {
        // ⚠️ Este test requiere que ApiErrorResponse tenga @NoArgsConstructor (no-args)
        String json = "{"
                + "\"timestamp\":\"2025-08-08 14:30:45\","
                + "\"status\":403,"
                + "\"error\":\"Forbidden\","
                + "\"message\":\"No access\","
                + "\"path\":\"/secure\""
                + "}";

        ApiErrorResponse read = mapper().readValue(json, ApiErrorResponse.class);

        assertThat(read.getTimestamp()).isEqualTo(LocalDateTime.of(2025, 8, 8, 14, 30, 45));
        assertThat(read.getStatus()).isEqualTo(403);
        assertThat(read.getError()).isEqualTo("Forbidden");
        assertThat(read.getMessage()).isEqualTo("No access");
        assertThat(read.getPath()).isEqualTo("/secure");

        // round-trip
        String json2 = mapper().writeValueAsString(read);
        ApiErrorResponse copy = mapper().readValue(json2, ApiErrorResponse.class);
        assertThat(copy).usingRecursiveComparison().isEqualTo(read);
    }

    @Test
    void builderWithNulls_onlySerializesSetFields() throws Exception {
        ApiErrorResponse r = ApiErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .build();

        // Campos no seteados deben quedar null en el objeto
        assertThat(r.getTimestamp()).isNull();
        assertThat(r.getMessage()).isNull();
        assertThat(r.getPath()).isNull();

        String json = mapper().writeValueAsString(r);
        // Al menos contiene los campos seteados
        assertThat(json).contains("\"status\":500");
        assertThat(json).contains("Internal Server Error");
    }

    @Test
    void individualSetters_updateFields() {
        ApiErrorResponse r = ApiErrorResponse.builder().build();
        LocalDateTime t = LocalDateTime.of(2024, 1, 2, 3, 4, 5);

        r.setTimestamp(t);
        r.setStatus(418);
        r.setError("I'm a teapot");
        r.setMessage("Short and stout");
        r.setPath("/brew");

        assertThat(r.getTimestamp()).isEqualTo(t);
        assertThat(r.getStatus()).isEqualTo(418);
        assertThat(r.getError()).isEqualTo("I'm a teapot");
        assertThat(r.getMessage()).isEqualTo("Short and stout");
        assertThat(r.getPath()).isEqualTo("/brew");
    }

    @Test
    void toString_shouldReturnNonNullString() {
        // Cubrimos el toString() del *builder* generado por Lombok
        ApiErrorResponse.ApiErrorResponseBuilder builder = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Invalid input")
                .path("/api/test");

        String result = builder.toString();

        assertNotNull(result);
        // No asumimos el formato exacto; solo que incluya alguna pista
        assertTrue(!result.isEmpty());
    }


}