
package com.celotts.productserviceOld.domain.port.product.brand.input;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;

import java.util.UUID;

public interface ProductBrandPort {

    boolean existsById(UUID id);
    ProductBrandResponseDto enableBrand(UUID id);
    ProductBrandResponseDto disableBrand(UUID id);

}