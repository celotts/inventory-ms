package com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productUnit;

import com.celotts.productserviceOld.domain.model.ProductUnitModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductUnitDtoMapper {

    public ProductUnitModel toModel(ProductUnitCreateDto createDto){
        if(createDto == null){
            return null;
        }

        return ProductUnitModel.builder()
                .code(createDto.getCode())
                .name(createDto.getName())
                .description(createDto.getDescription())
                .symbol(createDto.getSymbol())
                .enabled(createDto.getEnabled())
                .createdBy(createDto.getCreatedBy())
                .updatedBy(createDto.getUpdatedBy())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }

    public ProductUnitResponseDto toResponse(ProductUnitModel model) {
        if (model == null) return null;
        return ProductUnitResponseDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .name(model.getName())
                .description(model.getDescription())
                .enabled(model.getEnabled())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }


}
