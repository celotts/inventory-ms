package com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierCreateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierResponseDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierUpdateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.CentralMapperConfig;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SupplierModel toModel(SupplierCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateModelFromDto(@MappingTarget SupplierModel target, SupplierUpdateDto dto);

    @Mapping(target = "tax_id", source = "taxId")
    @Mapping(target = "active", source = "enabled")
    SupplierResponseDto toResponse(SupplierModel model);

    List<SupplierResponseDto> toResponseList(List<SupplierModel> models);

    default Page<SupplierResponseDto> toResponsePage(Page<SupplierModel> page) {
        return page.map(this::toResponse);
    }
}
