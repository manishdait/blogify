package com.example.blog.security;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/validate-access")
@RequiredArgsConstructor
public class SecureController {
  @GetMapping()
  public ResponseEntity<Map<String, Boolean>> getMethodName(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", true));
  }
}
