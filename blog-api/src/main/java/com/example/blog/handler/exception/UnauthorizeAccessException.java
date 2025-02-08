package com.example.blog.handler.exception;

import lombok.Getter;

public class UnauthorizeAccessException extends RuntimeException {
  @Getter
  private String message;

  public UnauthorizeAccessException(String message) {
    super(message);
    this.message = message;
  }
}
