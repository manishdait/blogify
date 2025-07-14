package com.example.blog.token;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.blog.user.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final TokenRepository tokenRepository;

  private Integer expiration = 900;

  @Transactional
  public String createToken(User user, TokenType type) {
    Token token = Token.builder()
      .expiration(Instant.now().plusSeconds(this.expiration))
      .user(user)
      .type(type)
      .token(generateToken())
      .used(false)
      .build();

    tokenRepository.save(token);
    return token.getToken();
  }

  @Transactional
  public boolean validToken(String token, String username, TokenType type) {
    Optional<Token> _token = tokenRepository.findByToken(token);

    if (_token.isEmpty() || !_token.get().getUser().getUsername().equals(username)) {
      throw new IllegalArgumentException("Invalid token");
    }

    if (_token.get().getType() != type || _token.get().isUsed()) {
      throw new IllegalArgumentException("Invalid token");
    }

    if (_token.get().getExpiration().isBefore(Instant.now())) {
      throw new IllegalArgumentException("Token is Expired");
    }
    
    _token.get().setUsed(true);
    tokenRepository.save(_token.get());
    return true;
  }

  @Transactional
  public String resendToken(User user, TokenType type) {
    List<Token> activeToken = tokenRepository.findByUserAndType(user, type).stream()
      .filter(t -> (!t.isUsed() && t.getExpiration().isAfter(Instant.now())))
      .toList();
    activeToken.stream().forEach(t -> t.setUsed(true));
    tokenRepository.saveAll(activeToken);

    return createToken(user, type);
  }

  private String generateToken() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    int len = 6;

    StringBuilder token = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < len; i++) {
      int index = random.nextInt(characters.length());
      token.append(characters.charAt(index));
    }

    return token.toString();
  }
}
