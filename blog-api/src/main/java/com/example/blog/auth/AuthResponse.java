package com.example.blog.auth;

public record AuthResponse(String username, String accessToken, String refreshToken) {
  
}
