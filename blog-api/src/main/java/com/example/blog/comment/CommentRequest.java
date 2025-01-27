package com.example.blog.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
  @NotNull
  @Schema(description = "Id of blog post")
  Integer blogId, 

  @NotBlank
  @Schema(description = "Comment message")
  String message
) {
  
}
