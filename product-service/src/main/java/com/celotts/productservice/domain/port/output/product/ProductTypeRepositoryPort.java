package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeRepositoryPort {
    ProductTypeModel save(ProductTypeModel model);
    Optional<ProductTypeModel> findById(UUID id);
    Optional<ProductTypeModel> findByCode(String code);

    Page<ProductTypeModel> findAll(Pageable pageable);  // paginado
    List<ProductTypeModel> findAll();                   // no paginado

    void deleteById(UUID id);

    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
}