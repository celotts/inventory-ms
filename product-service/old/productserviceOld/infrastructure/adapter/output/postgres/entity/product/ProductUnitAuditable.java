package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.audit.listener.AuditListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_unit")
@EntityListeners(AuditListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitAuditable implements Auditable {

    @Id
    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}