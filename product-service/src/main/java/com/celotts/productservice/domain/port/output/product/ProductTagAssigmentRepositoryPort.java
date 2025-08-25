package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagAssigmentRepositoryPort {
    ProductTagAssignmentModel save(ProductTagAssignmentModel model);
    Optional<ProductTagAssignmentModel> findById(UUID id);
    Optional<ProductTagAssignmentModel> findByName(String name);
    boolean existByName(String name);
    Page<ProductTagAssignmentModel>findAll(Pageable pageable);
    List<ProductTagAssignmentModel>findByEnabled(boolean enabled);
    long countByEnabled(boolean enabled);
    ProductTagAssignmentModel enable(UUID id);
    ProductTagAssignmentModel disable(UUID id);
    void deleteById(UUID id);

}
