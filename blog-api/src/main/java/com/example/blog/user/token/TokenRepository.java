package com.example.blog.user.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blog.user.User;


public interface TokenRepository extends JpaRepository<Token, Integer> {
  Optional<Token> findByCode(String code);
  List<Token> findByUser(User user);
}
