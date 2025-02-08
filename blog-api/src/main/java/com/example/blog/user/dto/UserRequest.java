package com.example.blog.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequest(
  @JsonProperty("first_name")
  @Schema(name = "first_name", description = "User first name")
  String firstName, 

  @JsonProperty("last_name")
  @Schema(name = "last_name", description = "User last name")
  String lastName
) {
 
}