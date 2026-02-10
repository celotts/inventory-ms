package com.celotts.authservice.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Response object containing the JWT token and user details")
public class JwtResponse {
    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "carlos.lott")
    private String username;

    @Schema(description = "User email", example = "carlos.lott@example.com")
    private String email;

    @Schema(description = "List of roles assigned to the user", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
