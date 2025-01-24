package com.example.blog.exception;

import lombok.Getter;

public class BlogApiException extends RuntimeException {
  @Getter
  private String message;

  public BlogApiException(String message) {
    super(message);
    this.message = message;
  }
}
