// Archivo: src/main/java/com/celotts/productservice/infrastructure/adapter/input/rest/mapper/productBrand/ProductBrandDtoMapper.java

package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductBrandDtoMapper {

    /**
     * Convierte ProductBrandCreateDto a ProductBrandModel
     * @param createDto DTO de entrada para creación
     * @return ProductBrandModel para el dominio
     */
    public ProductBrandModel toModel(ProductBrandCreateDto createDto) {
        if (createDto == null) {
            return null;
        }

        return ProductBrandModel.builder()
                .name(createDto.getName())
                .description(createDto.getDescription())
                .enabled(createDto.getEnabled())
                .createdBy(createDto.getCreatedBy())
                .updatedBy(createDto.getUpdatedBy())
                .createdAt(LocalDateTime.now())   // audit inicial
                .updatedAt(null)
                .build();
    }

    /**
     * Convierte ProductBrandModel a ProductBrandResponseDto
     * @param model Modelo del dominio
     * @return DTO de respuesta
     */
    public ProductBrandResponseDto toResponseDto(ProductBrandModel model) {
        if (model == null) {
            return null;
        }

        return ProductBrandResponseDto.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .enabled(model.getEnabled())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    /**
     * Convierte ProductBrandModel a ProductBrandCreateDto
     * Útil para operaciones de actualización
     * @param model Modelo del dominio
     * @return DTO de creación
     */
    //TODO: NO SE USA
    public ProductBrandCreateDto toCreateDto(ProductBrandModel model) {
        if (model == null) {
            return null;
        }

        ProductBrandCreateDto dto = new ProductBrandCreateDto();
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        return dto;
    }

    /**
     * Convierte lista de ProductBrandModel a lista de ProductBrandResponseDto
     * @param models Lista de modelos del dominio
     * @return Lista de DTOs de respuesta
     */
    //TODO: NO SE USA
    public List<ProductBrandResponseDto> toResponseDtoList(List<ProductBrandModel> models) {
        if (models == null) {
            return null;
        }

        return models.stream()
                .map(this::toResponseDto) // ✅ usar instancia actual
                .collect(Collectors.toList());
    }
}