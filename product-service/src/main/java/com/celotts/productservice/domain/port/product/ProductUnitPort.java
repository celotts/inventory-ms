package com.celotts.productservice.domain.port.product;

import java.util.List;
import java.util.Optional;

public interface ProductUnitPort {
    boolean existsByCode(String code);

    Optional<String> findNameByCode(String code);

    List<String> findAllCodes();
}