package com.celotts.authservice.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Request object for user registration")
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    @Schema(description = "Username for the new account", example = "new.user", required = true)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(description = "Email address for the new account", example = "new.user@example.com", required = true)
    private String email;

    @Schema(description = "Set of roles for the user (e.g., 'admin', 'mod', 'user')", example = "[\"user\", \"mod\"]")
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    @Schema(description = "Password for the new account", example = "securePass123!", required = true)
    private String password;
}
