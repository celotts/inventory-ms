
package com.celotts.productservice.domain.port.product;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandPort {

    boolean existsById(UUID id);
    ProductBrandResponseDto enableBrand(UUID id);
    ProductBrandResponseDto disableBrand(UUID id);
    //TODO: NO SE USA
    Optional<String> findNameById(UUID id);
    //TODO: NO SE USA
    List<UUID> findAllIds();
}