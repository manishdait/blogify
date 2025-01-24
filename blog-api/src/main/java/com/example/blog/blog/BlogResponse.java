package com.example.blog.blog;

import java.time.LocalDate;

public record BlogResponse(Integer id, String title, String content, String author, Integer comments, LocalDate createdAt) {
  
}
