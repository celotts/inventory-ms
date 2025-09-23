package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUnitCreateDto {

    @NotBlank
    @NotBlank(message = "Code is required")
    @Size(max = 30, message = "Code must not exceed 30 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,100}$",
            message = "Name has invalid characters"
    )
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters" )
    private String description;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @Size(max = 100, message = "CreatedBy max length is 100")
    @NotBlank(message = "CreatedBy is required")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "CreatedBy has invalid characters")
    private String createdBy;

    private String updatedBy;
}
