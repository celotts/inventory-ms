package com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierCreateDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierResponseDto;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier.SupplierUpdateDto;
import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier.SupplierEntity;
import com.celotts.supplierservice.infrastructure.adapter.input.rest.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        config = CentralMapperConfig.class,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface SupplierMapper {

    // ===== Request DTOs to Domain Model =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SupplierModel toModel(SupplierCreateDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SupplierModel toModel(SupplierUpdateDto updateDto);

    // ===== Domain Model to Response DTO =====

    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "taxId", target = "taxId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    SupplierResponseDto toResponse(SupplierModel model);

    // ===== Entity to Domain Model =====

    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "taxId", target = "taxId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    SupplierModel toDomain(SupplierEntity entity);

    // ===== Domain Model to Entity =====

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "taxId", target = "taxId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "enabled", target = "enabled")
    SupplierEntity toEntity(SupplierModel model);

    // ===== List Mappings =====

    List<SupplierResponseDto> toResponseList(List<SupplierModel> models);

    List<SupplierModel> toDomainList(List<SupplierEntity> entities);

    // ===== Page Mappings =====

    default Page<SupplierResponseDto> toResponsePage(Page<SupplierModel> page) {
        return page.map(this::toResponse);
    }

    default Page<SupplierModel> toDomainPage(Page<SupplierEntity> page) {
        return page.map(this::toDomain);
    }
}