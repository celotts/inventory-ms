package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productimage;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProductImageRequestDto extends PageableRequestDto {

    @NotNull(message = "ProductId is required")
    private UUID productId;

    private Boolean enabled;

    @PastOrPresent(message = "UploadedFrom must be in the past or present")
    private LocalDateTime uploadedFrom;

    @PastOrPresent(message = "UploadedTo must be in the past or present")
    private LocalDateTime uploadedTo;

    @Size(max = 100, message = "CreatedByLike max length is 100")
    private String createdByLike;
}