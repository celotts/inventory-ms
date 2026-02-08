package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockDto {
    private int stock;
}
