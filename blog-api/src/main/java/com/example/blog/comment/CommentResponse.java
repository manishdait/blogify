package com.example.blog.comment;

import java.time.LocalDate;

public record CommentResponse(Integer id, String message, String author, LocalDate createdAt) {
  
}
