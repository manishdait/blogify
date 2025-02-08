package com.example.blog.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
  @NotNull
  @Schema(name = "blog_id", description = "Id of blog post")
  @JsonProperty("blog_id")
  Integer blogId, 

  @NotBlank
  @Schema(description = "Comment message")
  String message
) {
  
}
