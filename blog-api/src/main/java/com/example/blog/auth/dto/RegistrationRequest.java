package com.example.blog.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
  @NotBlank
  @Schema(name = "first_name", description = "First name of user")
  @JsonProperty("first_name")
  String firstName, 

  @NotBlank
  @Schema(name = "last_name", description = "Last name of user")
  @JsonProperty("last_name")
  String lastName, 

  @Email
  @Schema(description = "Email address of user")
  String email, 

  @NotBlank
  @Size(min = 8, max = 20)
  @Schema(description = "Password for user of min 8 character")
  String password
) {
  
}
