package com.celotts.salesservice.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleModel {
    private UUID id;
    private String orderNumber;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private SaleStatus status;
    private OrderSource source;
    private String customerNote;
    private List<SaleItemModel> items;
}