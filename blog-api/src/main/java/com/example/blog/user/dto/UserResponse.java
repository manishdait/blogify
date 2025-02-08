package com.example.blog.user.dto;

import com.example.blog.util.image.Image;
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

  @JsonProperty("img_url")
  @Schema(name = "img_url", description = "User image url")
  String imgUrl
) {
  public UserResponse(String firstName, String lastName, String email, Image image) {
    this(firstName, lastName, email, image == null? null : "http://localhost:8080/blog-api/v1/image/" + image.getFilename());
  }
}
