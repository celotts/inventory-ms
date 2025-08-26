package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductTagAssignmentUseCase {

    ProductTagAssignmentModel create(ProductTagAssignmentModel model);

    ProductTagAssignmentModel update(UUID id, ProductTagAssignmentModel model);

    void delete(UUID id);

    ProductTagAssignmentModel findById(UUID id);

    /** Lista paginada (usa el repo Page<ProductTagAssignmentModel>) */
    Page<ProductTagAssignmentModel> findAll(Pageable pageable);

    /** Filtro por enabled (usa el repo List<ProductTagAssignmentModel>) */
    List<ProductTagAssignmentModel> findByEnabled(boolean enabled);

    /** Azúcar para el repo.countByEnabled(true) */
    long countEnabled();

    ProductTagAssignmentModel enable(UUID id);

    ProductTagAssignmentModel disable(UUID id);
}