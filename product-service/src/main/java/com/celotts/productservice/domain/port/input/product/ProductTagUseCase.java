package com.celotts.productservice.domain.port.input.product;

import com.celotts.productserviceOld.domain.model.ProductTagModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagUseCase {
    ProductTagModel create(ProductTagModel model);
    ProductTagModel update(UUID id, ProductTagModel model);
    void delete(UUID id);

    ProductTagModel enable(UUID id);
    ProductTagModel disable(UUID id);

    ProductTagModel findById(UUID id);
    Optional<ProductTagModel> findByName(String name);
    boolean existsByName(String name);

    Page<ProductTagModel> findAll(Pageable pageable);
    List<ProductTagModel> findAllEnabled();
    long countEnabled();
}