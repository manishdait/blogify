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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.exception.BlogApiException;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.mail.MailService;
import com.example.blog.security.JwtService;
import com.example.blog.user.User;
import com.example.blog.user.UserRepository;
import com.example.blog.user.UserService;
import com.example.blog.user.role.Role;
import com.example.blog.user.role.RoleRepository;
import com.example.blog.user.token.Token;
import com.example.blog.user.token.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final UserService userService;
  private final MailService mailService;
  private final JwtService jwtService;

  public AuthResponse registerUser(RegistrationRequest request) {
    userRepository.findByEmail(request.email()).ifPresent((u) -> {
      throw new BlogApiException(String.format("User with email:`%s` already present", u.getEmail()));
    });

    Role role = roleRepository.findByRole("Role_USER").orElseThrow();

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
      user.getUsername(), Map.of("fullName" , user.getFullname())
    );
    String refreshToken = generateRefreshToken(
      user.getUsername(), Map.of("fullName", user.getFullname())
    );

    String token = generateToken(user);
    mailService.sendEmailVerificationMail(user, token);

    return new AuthResponse(user.getUsername(), accessToken, refreshToken);
  }

  public AuthResponse authenticateUser(AuthRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.email(), 
        request.password()
      )
    );

    User user = (User) authentication.getPrincipal();

    String accessToken = jwtService.generateToken(
      user.getUsername(), Map.of("fullName", user.getFullname())
    );
    String refreshToken = generateRefreshToken(
      user.getUsername(), Map.of("fullName", user.getFullname())
    );

    return new AuthResponse(user.getUsername(), accessToken, refreshToken);
  }

  public void verifyAccount(String username, String code) {
    Token token = tokenRepository.findByCode(code).orElseThrow(
      () -> new ForbiddenException("Invalid token") 
    );

    if (token.getExpiration().isBefore(Instant.now())) {
      throw new ForbiddenException("Token is expired");
    }

    User user = token.getUser();
    if (!user.getUsername().equals(username)) {
      return;
    }

    user.setVerified(true);
    userRepository.save(user);
    
    token.setVerified(true);
    tokenRepository.save(token);
  }

  public void resendVerificationToken(String username) {
    User user = userRepository.findByEmail(username).orElseThrow(
      () -> new BlogApiException("Invalid username")
    );
    if (user.isVerified()) {
      throw new ForbiddenException("User already verified");
    }

    Optional<Token> _token = tokenRepository.findByUser(user).stream()
      .filter(t -> !t.isVerified())
      .findFirst();

    if (_token.isEmpty()) {
      String code = generateToken(user);
      mailService.sendEmailVerificationMail(user, code);
      return;
    }

    Token token = _token.get();
    token.setExpiration(Instant.now().plusSeconds(3600));
    tokenRepository.save(token);

    mailService.sendEmailVerificationMail(user, token.getCode());
  }

  public Optional<User> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof String) {
      return Optional.empty();
    }

    User user = (User) authentication.getPrincipal();
    return Optional.of(user);
  }

  public AuthResponse refreshToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (token == null || !token.startsWith("Bearer ")) {
      throw new ForbiddenException("Unauthorize request");
    }
    
    
    token = token.substring(7);
    String username = jwtService.getUsername(token);

    UserDetails userDetails = userService.loadUserByUsername(username);

    if(!jwtService.isValidToken(userDetails, token)) {
      new ForbiddenException("Invalid token");
    }

    String accessToken = jwtService.generateToken(
      username, Map.of("fullName", jwtService.getFullname(token))
    );
    return new AuthResponse(username, accessToken, token);
  }

  private String generateRefreshToken(String username, Map<String, Object> claims) {
    return jwtService.generateToken(username, claims, 604800);
  }

  private String generateToken(User user) {
    String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    SecureRandom random = new SecureRandom();
    StringBuilder code = new StringBuilder();

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(character.length());
      code.append(character.charAt(index));
    }

    Token token = Token.builder()
      .code(code.toString())
      .user(user)
      .expiration(Instant.now().plusSeconds(3600))
      .verified(false)
      .build();
    
    tokenRepository.save(token);
    return token.getCode();
  }
}
