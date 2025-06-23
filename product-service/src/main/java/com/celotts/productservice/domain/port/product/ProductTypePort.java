package com.celotts.productservice.domain.port.product;

import java.util.List;

public interface ProductTypePort {

    boolean existsByCode(String code);

    //TODO: cannot resolve method
    List<String> findAllCodes();
}