package com.example.blog.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.blog.exception.BlogApiException;
import com.example.blog.exception.ForbiddenException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ExceptionResponse> handleBadCredential(BadCredentialsException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.UNAUTHORIZED.value(),
        "Invalid Username or Password",
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.BAD_REQUEST.value(),
        "Bad Request",
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(BlogApiException.class)
  public ResponseEntity<ExceptionResponse> handleBlogApiException(BlogApiException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.UNAUTHORIZED.value(),
        e.getMessage(),
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ExceptionResponse> handleTokenException(ForbiddenException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.FORBIDDEN.value(),
        e.getMessage(),
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler({ExpiredJwtException.class, SignatureException.class})
  public ResponseEntity<ExceptionResponse> handleJwtException(Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.UNAUTHORIZED.value(),
        "Invalid acess token",
        request.getRequestURI()
      )
    );
  }
}
