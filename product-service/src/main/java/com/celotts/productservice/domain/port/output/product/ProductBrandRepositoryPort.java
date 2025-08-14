package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.ProductBrandModel;
import java.util.*;

public interface ProductBrandRepositoryPort {
    ProductBrandModel save(ProductBrandModel model);
    Optional<ProductBrandModel> findById(UUID id);
    Optional<ProductBrandModel> findByName(String name);
    List<ProductBrandModel> findAll();
    ProductBrandModel enable(UUID id);
    ProductBrandModel disable(UUID id);
}