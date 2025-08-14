package com.celotts.productservice.infrastructure.common;

import com.celotts.productserviceOld.infrastructure.common.ApplicationContextProvider;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationContextProviderTest {

    @Test
    void shouldSetAndGetApplicationContext() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        // Simula la inyección de contexto (Spring lo hace normalmente)
        ApplicationContextProvider contextProvider = new ApplicationContextProvider();
        contextProvider.setApplicationContext(mockContext);

        // Llamada explícita al método estático
        ApplicationContext retrievedContext = ApplicationContextProvider.getApplicationContext();

        // Validaciones
        assertNotNull(retrievedContext);
        assertEquals(mockContext, retrievedContext);
    }
}