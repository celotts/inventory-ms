package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productPriceHistory;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory.ProductPriceHistoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory.ProductPriceHistoryResponseDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductPriceHistoryDtoMapper {

    // Mantén este si en algún caso te llega productId en el body:
    public ProductPriceHistoryModel toModel(ProductPriceHistoryCreateDto dto) {
        if (dto == null) return null;

        return ProductPriceHistoryModel.builder()
                .productId(dto.getProductId())
                .price(dto.getPrice())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                // changedAt lo setea el use case si viene null
                .build();
    }

    // Overload recomendado: usar SIEMPRE el productId del path
    public ProductPriceHistoryModel toModel(ProductPriceHistoryCreateDto dto, UUID productIdFromPath) {
        if (dto == null) return null;

        return ProductPriceHistoryModel.builder()
                .productId(productIdFromPath) // <- manda el del path
                .price(dto.getPrice())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE)
                .build();
    }

    public ProductPriceHistoryResponseDto toResponse(ProductPriceHistoryModel m) {
        if (m == null) return null;

        return ProductPriceHistoryResponseDto.builder()
                .id(m.getId())
                .productId(m.getProductId())
                .price(m.getPrice())
                .enabled(m.getEnabled())
                .changedAt(m.getChangedAt())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .createdBy(m.getCreatedBy())
                .updatedBy(m.getUpdatedBy())
                .build();
    }
}