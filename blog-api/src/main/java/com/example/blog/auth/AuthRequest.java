package com.example.blog.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
  @Email
  @Schema(description = "Email address of user")
  String email, 

  @NotBlank
  @Size(min = 8, max = 20)
  @Schema(description = "Password for user of min 8 character")
  String password
) {
  
}
