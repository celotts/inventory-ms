package com.celotts.productservice.domain.port.output.product;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepositoryPort {
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
    // y los CRUD de ProductTypeModel si aplica
}