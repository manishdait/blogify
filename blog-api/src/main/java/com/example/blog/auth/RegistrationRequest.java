package com.example.blog.auth;

public record RegistrationRequest(String firstName, String lastName, String email, String password) {
  
}
