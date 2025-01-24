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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @PostMapping()
  public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request));
  }

  @GetMapping("/blog/{blogId}")
  public ResponseEntity<List<CommentResponse>> getCommentForBlog(@PathVariable Integer blogId) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentForBlog(blogId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> updateComment(@PathVariable Integer id, @RequestBody CommentRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
    commentService.deleteComment(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}
