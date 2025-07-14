package com.example.blog.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
  @JsonProperty("first_name")
  @Schema(name = "first_name", description = "User first name")
  String firstName, 

  @JsonProperty("last_name")
  @Schema(name = "last_name", description = "User last name")
  String lastName, 

  @JsonProperty("email")
  @Schema(name = "email", description = "User email")
  String email,

  @JsonProperty("profile")
  @Schema(name = "profile", description = "User image url")
  String profile
) {

}
