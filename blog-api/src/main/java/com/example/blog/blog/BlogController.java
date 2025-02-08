package com.example.blog.blog;

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

import com.example.blog.blog.dto.BlogRequest;
import com.example.blog.blog.dto.BlogResponse;
import com.example.blog.shared.PagedResponse;

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

  @PostMapping()
  @Operation(summary = "Create a new blog post")
  @SecurityRequirement(name = "accessToken")
  public ResponseEntity<BlogResponse> createBlog(@RequestBody BlogRequest request, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlog(request, authentication));
  }

  @GetMapping()
  @Operation(summary = "Get all blogs")
  public ResponseEntity<PagedResponse<BlogResponse>> getBlogs(
    @RequestParam(defaultValue = "0") int number, @RequestParam(defaultValue = "5") int size
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogs(number, size));
  }

  @GetMapping("/author/{author}")
  @Operation(summary = "Get all blogs for a author")
  @SecurityRequirement(name = "accessToken")
  public ResponseEntity<PagedResponse<BlogResponse>> getBlogsForAuthor(
    @PathVariable String author, @RequestParam(defaultValue = "0") int number, @RequestParam(defaultValue = "5") int size
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogsForAuthor(author, number, size));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a blog by blogId")
  @SecurityRequirement(name = "accessToken")
  public ResponseEntity<BlogResponse> getBlog(@PathVariable Integer id) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlog(id));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a blog")
  @SecurityRequirement(name = "accessToken")
  public ResponseEntity<BlogResponse> updateBlog(
    @PathVariable Integer id, @RequestBody BlogRequest request, Authentication authentication
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.updateBlog(id, request, authentication));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a blog")
  @SecurityRequirement(name = "accessToken")
  public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable Integer id, Authentication authentication) {
    blogService.deleteBlog(id, authentication);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("blog_id", id ,"deleted", true));
  }
}