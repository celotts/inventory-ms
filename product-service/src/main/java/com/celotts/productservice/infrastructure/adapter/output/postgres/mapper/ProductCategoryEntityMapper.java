package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Category;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductCategory;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ProductCategoryEntityMapper {

    public ProductCategoryModel toModel(ProductCategory entity) {
        return new ProductCategoryModel(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getCategory().getId(),
                entity.getCreatedAt()
        );
    }

    public ProductCategory toEntity(ProductCategoryModel model) {
        ProductEntity product = new ProductEntity();
        product.setId(model.productId());

        Category category = new Category();
        category.setId(model.categoryId());

        ProductCategory entity = new ProductCategory();
        entity.setId(model.id());
        entity.setProduct(product);
        entity.setCategory(category);
        entity.setCreatedAt(model.createdAt());

        return entity;
    }
}