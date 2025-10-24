package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.tax;

import com.celotts.supplierservice.infrastructure.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxRequestDto {
    @NotBlank(message = "{tax.code.pattern}", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[A-Z0-9\\-_]{3,40}$",
            message = "{validation.code.pattern}",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}
    )
    private String code;

    @NotBlank(message = "{tax.name.required}", groups = ValidationGroups.Create.class)
    private  String name;
}
