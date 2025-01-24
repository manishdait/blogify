package com.example.blog.handler;

import java.time.Instant;

public record ExceptionResponse(
  Instant timestamp,
  Integer status,
  String error,
  String path
) {}
