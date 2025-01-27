package com.example.blog.comment;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentResponse(
  @NotNull
  @Schema(description = "Id of comment")
  Integer id, 

  @NotBlank
  @Schema(description = "Comment message")
  String message, 

  @NotBlank
  @Schema(description = "Author of comment")
  String author, 

  @NotNull
  @Schema(description = "Created date of comment")
  LocalDate createdAt
) {
  
}
