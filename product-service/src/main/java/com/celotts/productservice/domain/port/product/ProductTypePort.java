package com.celotts.productservice.domain.port.product;

import java.util.List;
import java.util.Optional;

public interface ProductTypePort {

    boolean existsByCode(String code);
    //TODO: NO SE USA
    Optional<String> findNameByCode(String code);
    //TODO: NO SE USA
    List<String> findAllCodes();

}