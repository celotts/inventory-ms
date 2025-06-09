package com.celotts.productservice.domain.port.product;

import java.util.List;
import java.util.Optional;

public interface ProductTypePort {

    boolean existsByCode(String code);
    //TODO: cannot resolve method
    Optional<String> findNameByCode(String code);
    //TODO: cannot resolve method
    List<String> findAllCodes();
}