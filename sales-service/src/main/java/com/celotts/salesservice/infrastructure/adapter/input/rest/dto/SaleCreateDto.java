package com.celotts.salesservice.infrastructure.adapter.input.rest.dto;

import com.celotts.salesservice.domain.model.OrderSource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class SaleCreateDto {
    private String orderNumber;
    
    @NotNull(message = "Order source is required")
    private OrderSource source;
    
    private String customerNote;
    
    @NotEmpty(message = "Sale must have at least one item")
    private List<SaleItemCreateDto> items;
}