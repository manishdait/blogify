package com.example.blog.exception;

import lombok.Getter;

public class ForbiddenException extends RuntimeException {
  @Getter
  private String message;

  public ForbiddenException(String message) {
    super(message);
    this.message = message;
  }
}
