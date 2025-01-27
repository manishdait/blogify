package com.example.blog.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthResponse(
  @NotBlank
  @Schema(description = "Email address of user")
  String username, 

  @NotBlank
  @Schema(description = "Access token of user")
  String accessToken, 

  @NotBlank
  @Schema(description = "Refresh token of user")
  String refreshToken
) {
  
}
