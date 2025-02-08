package com.example.blog.auth;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.auth.dto.AuthRequest;
import com.example.blog.auth.dto.AuthResponse;
import com.example.blog.auth.dto.RegistrationRequest;
import com.example.blog.handler.exception.TokenException;
import com.example.blog.user.UserRepository;
import com.example.blog.user.User;
import com.example.blog.user.domain.role.Role;
import com.example.blog.user.domain.role.RoleRepository;
import com.example.blog.user.domain.token.Token;
import com.example.blog.user.domain.token.TokenRepository;
import com.example.blog.util.mail.Mail;
import com.example.blog.util.mail.MailContextBuilder;
import com.example.blog.util.mail.MailService;
import com.example.blog.util.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final MailService mailService;
  private final MailContextBuilder mailContextBuilder;
  private final JwtService jwtService;

  @Transactional
  public AuthResponse register(RegistrationRequest request) {
    userRepository.findByEmail(request.email()).ifPresent((u) -> {
      log.error("User with email:`{}` already present", u.getEmail());
      throw new IllegalArgumentException(String.format("User with email:`%s` already present", u.getEmail()));
    });

    Role role = roleRepository.findByRole("Role_USER").orElseThrow(() -> {
      log.error("Required role `{}` not exist", "Role_USER");
      throw new IllegalStateException("Required role not exist");
    });

    User user = User.builder()
      .firstName(request.firstName())
      .lastName(request.lastName())
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .verified(false)
      .roles(List.of(role))
      .build();

    userRepository.save(user);

    String accessToken = jwtService.generateToken(
      user.getUsername(), Map.of("full_name" , user.getFullname())
    );
    String refreshToken = generateRefreshToken(
      user.getUsername(), Map.of("full_name", user.getFullname())
    );

    String token = generateToken(user);
    Mail mail = mailContextBuilder.buildEmailVerificationMail(user, token);
    mailService.sendMail(mail);

    log.info("User register to system with username:`{}`", user.getUsername());
    return new AuthResponse(user.getUsername(), accessToken, refreshToken);
  }

  public AuthResponse authenticate(AuthRequest request) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      request.email(), 
      request.password()
    ));

    User user = (User) authentication.getPrincipal();

    String accessToken = jwtService.generateToken(
      user.getUsername(), Map.of("full_name", user.getFullname())
    );
    String refreshToken = generateRefreshToken(
      user.getUsername(), Map.of("full_name", user.getFullname())
    );

    return new AuthResponse(user.getUsername(), accessToken, refreshToken);
  }

  @Transactional
  public void verifyAccount(String username, String _token) {
    Token token = tokenRepository.findByToken(_token).orElseThrow(() -> {
      log.error("Invalid token during email verification");
      throw new TokenException("Invalid token");
    });

    if (token.getExpiration().isBefore(Instant.now())) {
      log.error("Expired token during email verification");
      throw new TokenException("Token is expired");
    }

    User user = token.getUser();
    if (!user.getUsername().equals(username) && token.isVerified()) {
      log.error("Invalid token during email verification");
      throw new TokenException("Invalid token");
    }

    user.setVerified(true);
    userRepository.save(user);
    
    token.setVerified(true);
    tokenRepository.save(token);
    log.info("User email verified:`{}`", username);
  }

  @Transactional
  public void resendVerificationToken(String username) {
    User user = userRepository.findByEmail(username).orElseThrow(() -> {
      throw new IllegalArgumentException("Invalid username");
    });
    if (user.isVerified()) {
      throw new TokenException("User already verified");
    }

    Optional<Token> _token = tokenRepository.findByUser(user).stream()
      .filter(t -> !t.isVerified())
      .findFirst();

    if (_token.isEmpty()) {
      String code = generateToken(user);
      Mail mail = mailContextBuilder.buildEmailVerificationMail(user, code);
      mailService.sendMail(mail);;
      return;
    }

    String code;
    do {
      code = generateToken();
    } while (tokenRepository.findByToken(code).isPresent());

    Token token = _token.get();
    token.setExpiration(Instant.now().plusSeconds(3600));
    token.setToken(code);
    tokenRepository.save(token);

    Mail mail = mailContextBuilder.buildEmailVerificationMail(user, token.getToken());
    mailService.sendMail(mail);
  }

  public AuthResponse refreshToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (token == null || !token.startsWith("Bearer ")) {
      throw new TokenException("Unauthorize request");
    }
    
    token = token.substring(7);
    String username = jwtService.username(token);

    UserDetails userDetails = userRepository.findByEmail(username).orElseThrow(
      () -> new TokenException("Invalid Token")
    );

    if(!jwtService.validToken(userDetails, token)) {
      new TokenException("Invalid token");
    }

    String accessToken = jwtService.generateToken(
      username, Map.of("full_name", jwtService.fullname(token))
    );
    return new AuthResponse(username, accessToken, token);
  }

  private String generateRefreshToken(String username, Map<String, Object> claims) {
    return jwtService.generateToken(username, claims, 604800);
  }

  private String generateToken(User user) {
    String _token;
    do {
      _token = generateToken();
    } while (tokenRepository.findByToken(_token).isPresent());

    Token token = Token.builder()
      .token(_token)
      .user(user)
      .expiration(Instant.now().plusSeconds(3600))
      .verified(false)
      .build();
    
    tokenRepository.save(token);
    return token.getToken();
  }

  private String generateToken() {
    String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    SecureRandom random = new SecureRandom();
    StringBuilder code = new StringBuilder();

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(character.length());
      code.append(character.charAt(index));
    }

    return code.toString();
  }
}
