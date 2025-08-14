package com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productUnit;

import com.celotts.productserviceOld.domain.model.ProductUnitModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductUnitResponseMapper {

    public ProductUnitResponseDto toDto(ProductUnitModel model) {
        if (model == null) return null;

        return ProductUnitResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .symbol(model.getSymbol())
                .enabled(model.getEnabled())
                .build();
    }

    public List<ProductUnitResponseDto> toDtoList(List<ProductUnitModel> models) {
        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }

        return models.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}