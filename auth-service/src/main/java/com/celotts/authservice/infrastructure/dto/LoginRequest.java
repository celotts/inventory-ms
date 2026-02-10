package com.celotts.authservice.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object for user login")
public class LoginRequest {
    @NotBlank
    @Schema(description = "Username of the user", example = "carlos.lott", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Password of the user", example = "password123", required = true)
    private String password;
}
