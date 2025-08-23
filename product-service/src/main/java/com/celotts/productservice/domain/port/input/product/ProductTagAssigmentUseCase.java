package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductTagAssigmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagAssigmentUseCase {
    ProductTagAssigmentModel create(ProductTagAssigmentModel model);
    ProductTagAssigmentModel update(UUID id, ProductTagAssigmentModel model);
    void delete(UUID id);

    ProductTagAssigmentModel enable(UUID id);
    ProductTagAssigmentModel disable(UUID id);

    ProductTagAssigmentModel findById(UUID id);
    Optional<ProductTagAssigmentModel> findByName(String name);
    boolean existsByName(String name);

    Page<ProductTagAssigmentModel> findAll(Pageable pageable);
    List<ProductTagAssigmentModel> findAllEnabled();
    long countEnabled();
}