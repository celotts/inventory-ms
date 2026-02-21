package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductTagAssignmentRepositoryPort {
    ProductTagAssignmentModel save(ProductTagAssignmentModel model);
    Optional<ProductTagAssignmentModel> findById(UUID id);
    Page<ProductTagAssignmentModel>findAll(Pageable pageable);
    Page<ProductTagAssignmentModel>findByEnabled(boolean enabled, Pageable pageable);
    long countByEnabled(boolean enabled);
    ProductTagAssignmentModel enable(UUID id);
    ProductTagAssignmentModel disable(UUID id);
    void deleteById(UUID id);

}
