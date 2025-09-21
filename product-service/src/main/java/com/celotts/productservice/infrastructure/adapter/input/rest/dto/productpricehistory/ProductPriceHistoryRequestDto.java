package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productpricehistory;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProductPriceHistoryRequestDto extends PageableRequestDto {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Price must have max 2 decimals")
    private BigDecimal price;

    private Boolean enabled;

    @PastOrPresent(message = "changedAt must be in the past or present")
    private LocalDateTime changedAt;
}