package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagAssignmentUseCase {
    ProductTagAssignmentModel create(ProductTagAssignmentModel model);
    ProductTagAssignmentModel findById(UUID id);
    ProductTagAssignmentModel update(UUID id, ProductTagAssignmentModel model);
    void delete(UUID id);
    Page<ProductTagAssignmentModel> findAll(Pageable pageable);
    Optional<ProductTagAssignmentModel> findByName(String name); // si tu dominio lo requiere
    List<ProductTagAssignmentModel> findAllEnabled();
    ProductTagAssignmentModel enable(UUID id);
    ProductTagAssignmentModel disable(UUID id);
    long countEnabled();
}