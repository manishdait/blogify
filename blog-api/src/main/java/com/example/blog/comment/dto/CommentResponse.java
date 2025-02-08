package com.example.blog.comment.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

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

  @JsonProperty("author_img")
  @Schema(name = "author_img", description = "Author image url")
  String authorImg, 

  @NotNull
  @Schema(name = "is_owned", description = "Comment owned by user")
  @JsonProperty("is_owned")
  Boolean isOwned,

  @NotNull
  @Schema(name = "created_at", description = "Created date of comment")
  @JsonProperty("created_at")
  Instant createdAt,

  @NotNull
  @Schema(name = "edited_at", description = "Edited date of comment")
  @JsonProperty("edited_at")
  Instant editedAt
) {
  
}
