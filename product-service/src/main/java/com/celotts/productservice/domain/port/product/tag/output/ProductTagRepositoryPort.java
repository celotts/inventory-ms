package com.celotts.productservice.domain.port.product.tag.output;

import com.celotts.productservice.domain.model.ProductTagModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagRepositoryPort {
    ProductTagModel save(ProductTagModel model);
    Optional<ProductTagModel> findById(UUID id);
    Optional<ProductTagModel> findByName(String name);
    boolean existsByName(String name);
    void deleteById(UUID id);

    Page<ProductTagModel> findAll(Pageable pageable);
    List<ProductTagModel> findByEnabled(boolean enabled);
    long countByEnabled(boolean enabled);
}