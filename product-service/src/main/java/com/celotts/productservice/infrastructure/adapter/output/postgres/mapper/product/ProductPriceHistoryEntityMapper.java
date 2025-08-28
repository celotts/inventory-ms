package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductPriceHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductPriceHistoryEntityMapper {

    // Entity → Model
    @Mapping(target = "productId", source = "product.id")
    ProductPriceHistoryModel toModel(ProductPriceHistoryEntity entity);

    // Model → Entity
    @Mapping(target = "product", source = "productId", qualifiedByName = "productRef")
    ProductPriceHistoryEntity toEntity(ProductPriceHistoryModel model);

    // Crea referencia ligera de ProductEntity con solo el id
    @Named("productRef")
    default ProductEntity productRef(java.util.UUID id) {
        if (id == null) return null;
        ProductEntity product = new ProductEntity();
        product.setId(id);
        return product;
    }
}