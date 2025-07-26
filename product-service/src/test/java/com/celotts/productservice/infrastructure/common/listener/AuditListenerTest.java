package com.celotts.productservice.infrastructure.common.listener;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.Auditable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuditListenerTest {

    private AuditListener listener;

    @BeforeEach
    void setUp() {
        listener = new AuditListener();
    }

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void prePersist_shouldSetAuditFieldsWithAuthenticatedUser() {
        Auditable mockAuditable = mock(Auditable.class);
        AuditListener listener = new AuditListener();

        // Simular autenticación
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Ejecutar
        listener.prePersist(mockAuditable);

        // Verificaciones
        verify(mockAuditable).setCreatedAt(any(LocalDateTime.class));
        verify(mockAuditable).setCreatedBy(eq("testUser"));
        verify(mockAuditable).setUpdatedAt(isNull());
        verify(mockAuditable).setUpdatedBy(isNull());
    }

    @Test
    void preUpdate_shouldSetAuditFieldsWithAuthenticatedUser() {
        Auditable entity = mock(Auditable.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user123");
        when(authentication.getPrincipal()).thenReturn("user123");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        listener.preUpdate(entity);

        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(entity).setUpdatedAt(dateCaptor.capture());
        verify(entity).setUpdatedBy("user123");

        assertThat(dateCaptor.getValue()).isNotNull();
    }

    @Test
    void preUpdate_shouldFallbackToSystemUserOnSecurityException() {
        Auditable entity = mock(Auditable.class);

        // Simular contexto de seguridad que lanza excepción
        SecurityContext brokenContext = mock(SecurityContext.class);
        when(brokenContext.getAuthentication()).thenThrow(new RuntimeException("Security error"));

        SecurityContextHolder.setContext(brokenContext);

        listener.preUpdate(entity);

        // Verificar que se usó "system" como usuario por defecto
        verify(entity).setUpdatedBy("system");
        verify(entity).setUpdatedAt(any(LocalDateTime.class));
    }

    @Test
    void getCurrentUser_shouldFallbackToSystem_whenExceptionIsThrown() {
        Auditable entity = mock(Auditable.class);

        // Simula SecurityContext que lanza excepción
        SecurityContext mockContext = mock(SecurityContext.class);
        when(mockContext.getAuthentication()).thenThrow(new RuntimeException("Simulated failure"));
        SecurityContextHolder.setContext(mockContext);

        listener.preUpdate(entity);

        // Verifica que se use "system" como fallback
        verify(entity).setUpdatedBy("system");
        verify(entity).setUpdatedAt(any(LocalDateTime.class));
    }

    @Test
    void getCurrentUser_shouldReturnSystem_whenSecurityContextFails() {
        Auditable entity = mock(Auditable.class);

        // Simula un SecurityContext cuyo getAuthentication lanza excepción
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenThrow(new RuntimeException("Simulated failure"));
        SecurityContextHolder.setContext(context);

        listener.preUpdate(entity);

        // Verifica que se use "system" como usuario por defecto
        verify(entity).setUpdatedBy("system");
        verify(entity).setUpdatedAt(any(LocalDateTime.class));
    }

    @Test
    void getCurrentUser_shouldReturnSystem_whenAuthenticationThrowsException() {
        Auditable entity = mock(Auditable.class);

        // Mock que lanza excepción al llamar a getAuthentication()
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenThrow(new RuntimeException("Simulado para cubrir catch"));

        // Asignamos el contexto simulado
        SecurityContextHolder.setContext(context);

        // Ejecutamos
        listener.preUpdate(entity);

        // Verificamos que se usó "system"
        verify(entity).setUpdatedBy("system");
        verify(entity).setUpdatedAt(any(LocalDateTime.class));
    }

    @Test
    void getCurrentUser_shouldReturnSystem_whenAuthenticationIsNull() {
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(context);

        AuditListener listenerTestable = new AuditListener(); // ← IMPORTANTE
        String result = listenerTestable.getCurrentUser();

        assertEquals("system", result);
    }

    @Test
    void getCurrentUser_shouldReturnSystem_whenAuthenticationNotAuthenticated() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        AuditListener listenerTestable = new AuditListener(); // ← Agrega esta línea

        String result = listenerTestable.getCurrentUser();
        assertEquals("system", result);
    }

    @Test
    void getCurrentUser_shouldReturnSystem_whenAuthenticationIsNull_case2() {
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        // ← INSTANCIA LA CLASE AQUÍ
        AuditListener listenerTestable = new AuditListener();

        String result = listenerTestable.getCurrentUser();
        assertEquals("system", result);
    }
}