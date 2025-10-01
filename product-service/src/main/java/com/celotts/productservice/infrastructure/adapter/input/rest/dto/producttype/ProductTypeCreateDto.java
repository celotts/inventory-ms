package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTypeCreateDto {

    @NotNull(message = "ProductTypeId is required")
    private UUID productTypeId;

    @NotNull(message = "Code is required")
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @Size(max = 255)
    String description;

    @Builder.Default
    Boolean enabled = true;

    @Size(max = 255, message = "CreatedBy max length is 255")
    @NotBlank
    String createdBy;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    String updatedAt;

    @Size(max = 255)
    private String updatedBy;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    String createdAt;

}
