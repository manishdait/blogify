package com.example.blog.util.security;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${security.jwt.secret-key}")
  private String key;

  @Value("${security.jwt.expiration}")
  private Integer expiration;

  public String generateToken(String username, Map<String, Object> claims) {
    return generateToken(username, claims, this.expiration);
  }

  public String generateToken(String username, Map<String, Object> claims, Integer expiration) {
    return Jwts.builder()
      .claims(claims)
      .subject(username)
      .issuedAt(Date.from(Instant.now()))
      .expiration(Date.from(Instant.now().plusSeconds(expiration)))
      .signWith(getKey())
      .compact();
  }

  public String username(String token) {
    return extractAllClaims(token).getSubject();
  }

  public String fullname(String token) {
    return extractAllClaims(token).get("full_name").toString();
  }

  public boolean tokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  public boolean validToken(UserDetails userDetails, String token) {
    return userDetails.getUsername().equals(username(token))
      && !tokenExpired(token);
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(key.getBytes());
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(getKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
