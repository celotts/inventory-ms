package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.ProductTagModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

public interface ProductTagRepositoryPort {
    ProductTagModel save(ProductTagModel model);
    Optional<ProductTagModel> findById(UUID id);
    Optional<ProductTagModel> findByName(String name);
    boolean existsByName(String name);
    Page<ProductTagModel> findAll(Pageable pageable);
    List<ProductTagModel> findByEnabled(boolean enabled);
    long countByEnabled(boolean enabled);
    ProductTagModel enable(UUID id);
    ProductTagModel disable(UUID id);
    void deleteById(UUID id);
}