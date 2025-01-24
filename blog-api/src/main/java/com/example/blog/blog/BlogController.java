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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/blog")
@RequiredArgsConstructor
public class BlogController {
  private final BlogService blogService;

  @PostMapping()
  public ResponseEntity<BlogResponse> createBlog(@RequestBody BlogRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlog(request));
  }

  @GetMapping()
  public ResponseEntity<List<BlogResponse>> getBlogs() {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogs());
  }

  @GetMapping("/author/{author}")
  public ResponseEntity<List<BlogResponse>> getBlogsForAuthor(@PathVariable String author) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogsForAuthor(author));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BlogResponse> getBlog(@PathVariable Integer id) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlog(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BlogResponse> updateBlog(@PathVariable Integer id, @RequestBody BlogRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(blogService.updateBlog(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBlog(@PathVariable Integer id) {
    blogService.deleteBlog(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}