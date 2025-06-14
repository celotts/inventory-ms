package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductBrandEntityMapper {

    public ProductBrandEntity toEntity(ProductBrandModel productBrandModel) { // ✅ Cambiar ProductBrandEntity por ProductBrand
        if (productBrandModel == null) {
            return null;
        }

        return ProductBrandEntity.builder()
                .id(productBrandModel.getId())
                .name(productBrandModel.getName())
                .description(productBrandModel.getDescription())
                .build();
    }

    public ProductBrandModel toModel(ProductBrandEntity entity) { // ✅ Cambiar ProductBrandEntity por ProductBrand
        if (entity == null) {
            return null;
        }

        return ProductBrandModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}