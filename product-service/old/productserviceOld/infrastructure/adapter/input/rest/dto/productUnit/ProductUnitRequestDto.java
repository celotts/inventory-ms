package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ProductUnitRequestDto {

     @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
     @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,100}$", message = "Product name contains invalid characters")
     private String name;

     @Size(max = 500, message = "Description must not exceed 500 characters")
     private String description;

     @Column(nullable = false)
     private Boolean enabled = true;

     private String createdBy;

     private String updatedBy;

}
