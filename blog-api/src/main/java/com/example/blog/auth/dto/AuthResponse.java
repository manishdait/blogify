package com.example.blog.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthResponse(
  @NotBlank
  @Schema(description = "Email address of user")
  String username, 

  @NotBlank
  @Schema(name = "access_token", description = "Access token of user")
  @JsonProperty("access_token")
  String accessToken, 

  @NotBlank
  @Schema(name = "refresh_token", description = "Refresh token of user")
  @JsonProperty("refresh_token")
  String refreshToken
) {
  
}
