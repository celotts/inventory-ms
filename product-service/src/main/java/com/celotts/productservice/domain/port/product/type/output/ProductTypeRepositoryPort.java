package com.celotts.productservice.domain.port.product.type.output;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeRepositoryPort {

    ProductUnitResponseDto create(ProductUnitCreateDto dto);
    List<ProductUnitResponseDto> findAll();
    ProductUnitResponseDto findById(UUID id);
    ProductUnitResponseDto update(UUID id, ProductUnitUpdateDto dto);
    void delete(UUID id);

    // consultas light
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
}