package com.celotts.productservice.infrastructure.adapter.input.rest.exception;

import com.celotts.productservice.infrastructure.adapter.input.rest.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.lang.reflect.Method;
import java.util.Map;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest req;

    private static class Dummy {
        @jakarta.validation.Valid
        void m(String name) { /* no-op */ }
    }

    @BeforeEach
    void setup() throws Exception {
        handler = new GlobalExceptionHandler();
        // inyecta appName
        var f = GlobalExceptionHandler.class.getDeclaredField("appName");
        f.setAccessible(true);
        f.set(handler, "product-service");

        req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    void notFound_returns_404_problem() {
        ResponseEntity<ProblemDetail> resp =
                handler.notFound(new ResourceNotFoundException("Thing", "1"), req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Not Found");
        assertThat(resp.getBody().getProperties()).containsEntry("code", "ERR_NOT_FOUND");
        assertThat(resp.getBody().getProperties()).containsEntry("service", "product-service");
    }

    @Test
    void conflict_returns_409_problem() {
        // CORREGIDO: tu excepción requiere (resource, idOrKey)
        ResponseEntity<ProblemDetail> resp =
                handler.conflict(new ResourceAlreadyExistsException("Thing", "1"), req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Conflict");
        assertThat(resp.getBody().getProperties()).containsEntry("code", "ERR_CONFLICT");
    }

    @Test
    void badJson_returns_400_problem() {
        // Usa el ctor NO deprecado: (String, Throwable, HttpInputMessage)
        ResponseEntity<ProblemDetail> resp =
                handler.badJson(
                        new HttpMessageNotReadableException("bad json", new RuntimeException(), (HttpInputMessage) null),
                        req
                );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail pd = resp.getBody();
        assertThat(pd).isNotNull();
        assertThat(pd.getTitle()).isEqualTo("Invalid JSON");
        assertThat(pd.getProperties()).containsEntry("code", "ERR_JSON");
    }

    @Test
    void dataIntegrity_returns_400_problem() {
        ResponseEntity<ProblemDetail> resp =
                handler.dataIntegrity(new DataIntegrityViolationException("db"), req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Data Integrity Violation");
    }

    @Test
    void illegalArg_returns_400_problem() {
        ResponseEntity<ProblemDetail> resp =
                handler.illegalArg(new IllegalArgumentException("bad"), req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Invalid Argument");
    }

    @Test
    void unexpected_returns_500_problem() {
        ResponseEntity<ProblemDetail> resp =
                handler.unexpected(new RuntimeException("oops"), req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Internal Server Error");
    }

    @Test
    void typeMismatch_returns_400_problem() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("123", Integer.class, "id", null, new IllegalArgumentException());

        ResponseEntity<ProblemDetail> resp = handler.typeMismatch(ex, req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Bad Request");
        assertThat(resp.getBody().getProperties()).containsEntry("code", "ERR_PARAM_TYPE");
    }

    @Test
    void validation_returns_400_with_validationErrors_map() throws Exception {
        // Construye un BindingResult con un FieldError simulado
        Object target = new Object();
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(target, "dto");
        br.addError(new FieldError("dto", "name", "must not be blank"));
        br.addError(new FieldError("dto", "enabled", "must be true or false"));

        // Crea un MethodParameter de un método dummy con parámetro @Valid
        Method method = Dummy.class.getDeclaredMethod("m", String.class);
        MethodParameter mp = new MethodParameter(method, 0);

        // Crea la excepción que maneja el handler
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(mp, br);

        // Ejecuta el handler
        ResponseEntity<ProblemDetail> resp = handler.validation(ex, req);

        // Asserts
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail pd = resp.getBody();
        assertThat(pd).isNotNull();
        assertThat(pd.getTitle()).isEqualTo("Validation Failed");
        assertThat(pd.getDetail())
                .isEqualTo("Los datos enviados no cumplen con las validaciones requeridas");
        assertThat(pd.getProperties()).containsEntry("code", "ERR_VALIDATION");
        assertThat(pd.getProperties()).containsEntry("service", "product-service");
        assertThat(pd.getProperties()).containsEntry("path", "/api/v1/test");

        // Verifica el mapa "validationErrors"
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) pd.getProperties().get("validationErrors");
        assertThat(errors)
                .containsEntry("name", "must not be blank")
                .containsEntry("enabled", "must be true or false");
    }
}