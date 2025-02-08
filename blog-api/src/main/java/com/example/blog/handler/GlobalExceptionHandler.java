package com.example.blog.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.blog.handler.exception.TokenException;
import com.example.blog.handler.exception.UnauthorizeAccessException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ExceptionResponse> handleBadCredential(BadCredentialsException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.FORBIDDEN.value(),
        "Invalid Username or Password",
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  public ResponseEntity<ExceptionResponse> handleIllegalArgumentAndState(Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.BAD_REQUEST.value(),
        e.getMessage(),
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.NOT_FOUND.value(),
        e.getMessage(),
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(TokenException.class)
  public ResponseEntity<ExceptionResponse> handleTokenException(TokenException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.FORBIDDEN.value(),
        e.getMessage(),
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(UnauthorizeAccessException.class)
  public ResponseEntity<ExceptionResponse> handleUnauthorizeAccess(UnauthorizeAccessException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.FORBIDDEN.value(),
        e.getMessage(),
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler({JwtException.class, ExpiredJwtException.class, SignatureException.class})
  public ResponseEntity<ExceptionResponse> handleJwtException(Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.UNAUTHORIZED.value(),
        "Invalid accessToken",
        request.getRequestURI()
      )
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleexception(Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      new ExceptionResponse(
        Instant.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Something went wrong",
        request.getRequestURI()
      )
    );
  }
}
