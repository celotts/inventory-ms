package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductTagAssigmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagAssigmentRepositoryPort {
    ProductTagAssigmentModel save(ProductTagAssigmentModel model);
    Optional<ProductTagAssigmentModel> findById(UUID id);
    Optional<ProductTagAssigmentModel> findByName(String name);
    boolean existByName(String name);
    Page<ProductTagAssigmentModel>findAll(Pageable pageable);
    List<ProductTagAssigmentModel>findByEnabled(boolean enabled);
    long countByEnabled(boolean enabled);
    ProductTagAssigmentModel enable(UUID id);
    ProductTagAssigmentModel disable(UUID id);
    void deleteById(UUID id);

}
