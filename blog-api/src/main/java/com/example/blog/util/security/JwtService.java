package com.example.blog.util.security;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${spring.security.secret-key}")
  private String key;

  @Value("${spring.security.expiration}")
  private Integer expiration;

  public String generateToken(String username, Integer expiration) {
    return Jwts.builder()
      .claims(new HashMap<>())
      .subject(username)
      .issuedAt(Date.from(Instant.now()))
      .expiration(Date.from(Instant.now().plusSeconds(expiration)))
      .signWith(getKey())
      .compact();
  }

  public String generateToken(String username) {
    return generateToken(username, this.expiration);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(getKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String getUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  public boolean tokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  public boolean validToken(UserDetails userDetails, String token) {
    return userDetails.getUsername().equals(getUsername(token))
      && !tokenExpired(token);
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(this.key.getBytes());
  }
}
