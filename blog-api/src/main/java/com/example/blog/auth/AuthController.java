package com.example.blog.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.auth.dto.AuthRequest;
import com.example.blog.auth.dto.AuthResponse;
import com.example.blog.auth.dto.RegistrationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Operation(summary = "Register a new user")
  @PostMapping("/sign-up")
  public ResponseEntity<AuthResponse> registerUser(@RequestBody RegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
  }

  @Operation(summary = "Authenticate the user")
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(request));
  }

  @Operation(summary = "Verify email address of user")
  @PostMapping("/verify/{username}")
  public ResponseEntity<Map<String, Boolean>> verifyUser(@PathVariable String username, @RequestParam(required = true) String token) {
    authService.verifyEmail(username, token);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("verified", true));
  }

  @Operation(summary = "Resend new verification token to user")
  @PostMapping("/resend-token")
  public ResponseEntity<Map<String, Boolean>> resendVerificationToken(@RequestParam(required = true) String username) {
    authService.resendVerificationToken(username);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("resendToken", true));
  }

  @Operation(summary = "Refresh accessToken for user")
  @SecurityRequirement(name = "refreshToken")
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(request));
  }
}
