package com.celotts.salesservice.infrastructure.adapter.output.persistence.entity;

import com.celotts.salesservice.domain.model.OrderSource;
import com.celotts.salesservice.domain.model.SaleStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SaleEntity {
    @Id
    private UUID id;
    
    @Column(unique = true)
    private String orderNumber;
    
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    private SaleStatus status;
    
    @Enumerated(EnumType.STRING)
    private OrderSource source;
    
    private String customerNote;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItemEntity> items;
}