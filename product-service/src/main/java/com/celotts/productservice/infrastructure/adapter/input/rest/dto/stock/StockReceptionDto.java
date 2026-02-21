package com.celotts.productservice.infrastructure.adapter.input.rest.dto.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for receiving stock from a purchase or return")
public class StockReceptionDto {

    @NotNull
    @Schema(description = "ID of the product to receive")
    private UUID productId;

    @NotNull
    @Positive
    @Schema(description = "Quantity received")
    private BigDecimal quantity;

    @NotNull
    @Positive
    @Schema(description = "Unit cost of the product in this reception")
    private BigDecimal unitCost;

    @Schema(description = "Lot code (optional, auto-generated if null)")
    private String lotCode;

    @Schema(description = "Expiration date (optional)")
    private LocalDate expirationDate;

    @Schema(description = "Reference to the source (e.g., Purchase Order Number)")
    private String referenceNumber;

    @Schema(description = "ID of the supplier (optional)")
    private UUID supplierId;
}
