package com.example.blog.blog.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BlogResponse(
  @NotNull
  @Schema(description = "Id of blog")
  Integer id, 

  @NotBlank
  @Schema(description = "Title of blog post")
  String title, 

  @NotBlank
  @Schema(description = "Content of blog post")
  String content, 

  @NotBlank
  @Schema(description = "Author of blog post")
  String author, 

  @JsonProperty("author_img")
  @Schema(name = "author_img", description = "Author image url")
  String authorImg, 

  @NotNull
  @Schema(description = "Comment count for blog post")
  Integer comments, 

  @NotNull
  @Schema(name = "created_at", description = "Created date for blog post")
  @JsonProperty("created_at")
  Instant createdAt,

  @NotNull
  @Schema(name = "edited_at", description = "Edited date for blog post")
  @JsonProperty("edited_at")
  Instant editedAt
) {
  
}
