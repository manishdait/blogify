package com.example.blog.blog;

import java.time.LocalDate;

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

  @NotNull
  @Schema(description = "Comment count for blog post")
  Integer comments, 

  @NotNull
  @Schema(description = "Created date for blog post")
  LocalDate createdAt
) {
  
}
