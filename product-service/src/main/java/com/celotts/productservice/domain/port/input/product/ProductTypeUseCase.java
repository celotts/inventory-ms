package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeUseCase {
    ProductTypeModel create(ProductTypeModel model);

    Optional<ProductTypeModel> getById(UUID id);
    Optional<ProductTypeModel> getByCode(String code);
    Page<ProductTypeModel> getAll(Pageable pageable);

    ProductTypeModel update(UUID id, ProductTypeModel changes);
    ProductTypeModel patch(UUID id, ProductTypeModel partial);
    void delete(UUID id);

    // Helpers (opcionales)
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
}