package com.celotts.productservice.domain.port;

import java.util.List;
import java.util.Optional;

public interface ProductTypePort {

    boolean existsByCode(String code);

    Optional<String> findNameByCode(String code);

    List<String> findAllCodes();
}