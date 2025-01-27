package com.example.blog.blog;

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
@RequestMapping("/blog-api/v1/blog")
@Tag(name = "Blogs", description = "API endpoints for managing blog posts")
@RequiredArgsConstructor
public class BlogController {
  private final BlogService blogService;

  @Operation(summary = "Create a new blog post")
  @SecurityRequirement(name = "accessToken")
  @PostMapping()
  public ResponseEntity<BlogResponse> createBlog(@RequestBody BlogRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlog(request));
  }

  @Operation(summary = "Get all blogs")
  @GetMapping()
  public ResponseEntity<List<BlogResponse>> getBlogs() {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogs());
  }

  @Operation(summary = "Get all blogs for a author")
  @SecurityRequirement(name = "accessToken")
  @GetMapping("/author/{author}")
  public ResponseEntity<List<BlogResponse>> getBlogsForAuthor(@PathVariable String author) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogsForAuthor(author));
  }

  @Operation(summary = "Get a blog by blogId")
  @SecurityRequirement(name = "accessToken")
  @GetMapping("/{id}")
  public ResponseEntity<BlogResponse> getBlog(@PathVariable Integer id) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlog(id));
  }

  @Operation(summary = "Update a blog")
  @SecurityRequirement(name = "accessToken")
  @PutMapping("/{id}")
  public ResponseEntity<BlogResponse> updateBlog(@PathVariable Integer id, @RequestBody BlogRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.updateBlog(id, request));
  }

  @Operation(summary = "Delete a blog")
  @SecurityRequirement(name = "accessToken")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBlog(@PathVariable Integer id) {
    blogService.deleteBlog(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}