package com.celotts.productservice.infrastructure.common.listener;

import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
//TODO: cannot resolve method
@Slf4j
public class AuditListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Auditable auditable) {
            LocalDateTime now = LocalDateTime.now();
            String currentUser = getCurrentUser();

            log.debug("PrePersist: Setting audit fields for entity: {}", entity.getClass().getSimpleName());

            auditable.setCreatedAt(now);
            auditable.setCreatedBy(currentUser);
            auditable.setUpdatedAt(null);
            auditable.setUpdatedBy(null);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Auditable auditable) {
            LocalDateTime now = LocalDateTime.now();
            String currentUser = getCurrentUser();

            log.debug("PreUpdate: Setting audit fields for entity: {}", entity.getClass().getSimpleName());

            auditable.setUpdatedAt(now);
            auditable.setUpdatedBy(currentUser);
        }
    }

    private String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getPrincipal())) {
                return authentication.getName();
            }
        } catch (Exception e) {
            log.debug("Could not get current user from security context: {}", e.getMessage());
        }
        return "system";
    }
}