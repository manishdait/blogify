package com.example.blog.comment;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.comment.dto.CommentRequest;
import com.example.blog.comment.dto.CommentResponse;
import com.example.blog.shared.PagedResponse;

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
  public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request, Authentication authentication) {
    System.out.println("Call comment");
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request, authentication));
  }

  @Operation(summary = "Get all comments for a blog post")
  @SecurityRequirement(name = "accessToken")
  @GetMapping("/blog/{blogId}")
  public ResponseEntity<PagedResponse<CommentResponse>> getCommentForBlog(
    @PathVariable Integer blogId, Authentication authentication, @RequestParam(defaultValue = "0") int number, 
    @RequestParam(defaultValue = "5") int size
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentForBlog(blogId, authentication, number, size));
  }

  @Operation(summary = "Update a comment")
  @SecurityRequirement(name = "accessToken")
  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> updateComment(
    @PathVariable Integer id, @RequestBody CommentRequest request, Authentication authentication
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(id, request, authentication));
  }

  @Operation(summary = "Delete a comment")
  @SecurityRequirement(name = "accessToken")
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Integer id, Authentication authentication) {
    commentService.deleteComment(id, authentication);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("comment_id", id, "deleted", true));
  }
}
