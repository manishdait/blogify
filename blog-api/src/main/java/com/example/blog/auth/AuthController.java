package com.example.blog.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign-up")
  public ResponseEntity<AuthResponse> registerUser(@RequestBody RegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(request));
  }

  @PostMapping("/verify/{username}/{token}")
  public ResponseEntity<Map<String, Boolean>> verifyUser(@PathVariable String username, @PathVariable String token) {
    authService.verifyAccount(username, token);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("verified", true));
  }

  @PostMapping("/resend-token/{username}")
  public ResponseEntity<Map<String, Boolean>> resendVerificationToken(@PathVariable String username) {
    authService.resendVerificationToken(username);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("resendToken", true));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(request));
  }
}
