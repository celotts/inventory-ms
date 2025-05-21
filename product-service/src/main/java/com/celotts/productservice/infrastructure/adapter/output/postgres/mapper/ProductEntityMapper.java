package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Product;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductBrand;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductType;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductUnit;

public class ProductEntityMapper {

    public static Product toEntity(ProductModel model) {
        Product product = new Product();
        product.setId(model.getId());
        product.setCode(model.getCode());
        product.setDescription(model.getDescription());

        ProductType type = new ProductType();
        type.setCode(model.getProductTypeCode());
        product.setProductType(type);

        ProductUnit unit = new ProductUnit();
        unit.setCode(model.getUnitCode());
        product.setProductUnit(unit);

        ProductBrand brand = new ProductBrand();
        brand.setId(model.getBrandId());
        product.setProductBrand(brand);

        product.setMinimumStock(model.getMinimumStock());
        product.setCurrentStock(model.getCurrentStock());
        product.setUnitPrice(model.getUnitPrice());

        product.setEnabled(model.getEnabled());
        product.setCreatedAt(model.getCreatedAt());
        product.setUpdatedAt(model.getUpdatedAt());
        product.setCreatedBy(model.getCreatedBy());
        product.setUpdatedBy(model.getUpdatedBy());

        return product;
    }

    public static ProductModel toModel(Product entity) {
        return ProductModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .description(entity.getDescription())
                .productTypeCode(entity.getProductType() != null ? entity.getProductType().getCode() : null)
                .unitCode(entity.getProductUnit() != null ? entity.getProductUnit().getCode() : null)
                .brandId(entity.getProductBrand() != null ? entity.getProductBrand().getId() : null)
                .minimumStock(entity.getMinimumStock())
                .currentStock(entity.getCurrentStock())
                .unitPrice(entity.getUnitPrice())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
