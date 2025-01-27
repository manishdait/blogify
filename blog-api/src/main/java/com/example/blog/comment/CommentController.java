package com.example.blog.comment;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/comment")
@Tag(name = "Comment", description = "APIs for managing comments on blog posts")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @Operation(summary = "Create a comment for blog post")
  @SecurityRequirement(name = "accessToken")
  @PostMapping()
  public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request));
  }

  @Operation(summary = "Get all comments for a blog post")
  @SecurityRequirement(name = "accessToken")
  @GetMapping("/blog/{blogId}")
  public ResponseEntity<List<CommentResponse>> getCommentForBlog(@PathVariable Integer blogId) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentForBlog(blogId));
  }

  @Operation(summary = "Update a comment")
  @SecurityRequirement(name = "accessToken")
  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> updateComment(@PathVariable Integer id, @RequestBody CommentRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(id, request));
  }

  @Operation(summary = "Delete a comment")
  @SecurityRequirement(name = "accessToken")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
    commentService.deleteComment(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
