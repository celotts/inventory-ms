package com.celotts.taxservice.infrastructure.adapter.input.rest.mapper.tax;

import com.celotts.taxservice.domain.model.tax.TaxModel;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxCreateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxUpdateDto;
import com.celotts.taxservice.infrastructure.adapter.input.rest.dto.tax.TaxResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaxMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "createdBy", target = "createdBy") // Corregido
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    TaxModel createdFrom(TaxCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateFrom(TaxUpdateDto dto, @MappingTarget TaxModel model);

    TaxResponseDto toResponse(TaxModel model);

    List<TaxResponseDto> toResponseList(List<TaxModel> models);
}
