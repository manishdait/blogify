package com.example.blog.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${jwt.secret-key}")
  private String key;

  @Value("${jwt.expiration-time}")
  private Integer expiration;

  public String generateToken(String username, Map<String, Object> claims) {
    return generateToken(username, claims, this.expiration);
  }

  public String generateToken(String username, Map<String, Object> claims, Integer expiration) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(username)
      .setIssuedAt(Date.from(Instant.now()))
      .setExpiration(Date.from(Instant.now().plusSeconds(expiration)))
      .signWith(getKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public Claims parseToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public String getUsername(String token) {
    return parseToken(token).getSubject();
  }

  public String getFullname(String token) {
    return parseToken(token).get("fullName").toString();
  }

  public boolean isExpiredToken(String token) {
    return parseToken(token).getExpiration().before(new Date());
  }

  public boolean isValidToken(UserDetails userDetails, String token) {
    return userDetails.getUsername().equals(getUsername(token))
      && !isExpiredToken(token);
  }

  private Key getKey() {
    byte[] decodedKey = Decoders.BASE64.decode(this.key);
    return Keys.hmacShaKeyFor(decodedKey);
  }
}
