package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.CentralMapperConfig;
import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductPriceHistoryEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(config = CentralMapperConfig.class)
public interface ProductPriceHistoryEntityMapper {

    // Entity -> Model
    @Mapping(target = "productId", source = "product.id")
    ProductPriceHistoryModel toModel(ProductPriceHistoryEntity entity);

    // List<Entity> -> List<Model>
    List<ProductPriceHistoryModel> toModelList(List<ProductPriceHistoryEntity> entities);

    /**
     * Model -> Entity (CREATE)
     * - Ignoramos id (DB lo genera con gen_random_uuid()).
     * - Audit/soft-delete los maneja listener/DB.
     * - product se resuelve a referencia ligera por UUID (sin hit a DB).
     */
    @BeanMapping(ignoreByDefault = false) // usa config global; explícitos los ignores clave
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "product", source = "productId", qualifiedByName = "productRef"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "deletedBy", ignore = true),
            @Mapping(target = "deletedReason", ignore = true)
            // Si changedAt lo pone la DB, ignóralo también:
            // ,@Mapping(target = "changedAt", ignore = true)
            // Si enabled tiene default en DB y no quieres que el modelo lo fuerce:
            // ,@Mapping(target = "enabled", ignore = true)
    })
    ProductPriceHistoryEntity toEntity(ProductPriceHistoryModel model);

    /**
     * Referencia ligera de ProductEntity con solo el id (evita carga innecesaria).
     */
    @Named("productRef")
    default ProductEntity productRef(UUID id) {
        if (id == null) return null;
        ProductEntity p = new ProductEntity();
        p.setId(id);
        return p;
    }


}