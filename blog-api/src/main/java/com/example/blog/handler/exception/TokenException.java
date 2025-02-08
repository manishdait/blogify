package com.example.blog.handler.exception;

import lombok.Getter;

public class TokenException extends RuntimeException {
  @Getter
  private String message;

  public TokenException(String message) {
    super(message);
    this.message = message;
  }
}
