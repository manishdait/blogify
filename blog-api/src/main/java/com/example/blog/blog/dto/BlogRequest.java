package com.example.blog.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record BlogRequest(
  @NotBlank
  @Schema(description = "Title for blog post")
  String title, 

  @NotBlank
  @Schema(description = "Content of blog post")
  String content
) {
  
}
