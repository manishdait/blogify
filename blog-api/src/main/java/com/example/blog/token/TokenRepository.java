package com.example.blog.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.user.User;

public interface TokenRepository extends JpaRepository<Token, Integer> {
  Optional<Token> findByToken(String token);
  List<Token> findByUserAndType(User user, TokenType type);
}
