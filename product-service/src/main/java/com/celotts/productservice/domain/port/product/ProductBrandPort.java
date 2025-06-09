
package com.celotts.productservice.domain.port.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandPort {

    boolean existsById(UUID id);
    //TODO: no usages
    Optional<String> findNameById(UUID id);
    //TODO: no usages
    List<UUID> findAllIds();
}