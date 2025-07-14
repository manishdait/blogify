package com.example.blog.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blog.auth.dto.AuthRequest;
import com.example.blog.auth.dto.AuthResponse;
import com.example.blog.auth.dto.RegistrationRequest;
import com.example.blog.token.TokenService;
import com.example.blog.token.TokenType;
import com.example.blog.user.User;
import com.example.blog.user.UserRepository;
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
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final TokenService tokenService;
  private final MailService mailService;
  private final MailContextBuilder mailContextBuilder;
  private final JwtService jwtService;

  @Transactional
  public AuthResponse registerUser(RegistrationRequest request) {
    userRepository.findByEmail(request.email()).ifPresent((u) -> {
      log.error("User with email:`{}` already present", u.getEmail());
      throw new IllegalArgumentException(String.format("User with email:`%s` already present", u.getEmail()));
    });

    User user = User.builder()
      .firstName(request.firstName())
      .lastName(request.lastName())
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .verified(false)
      .build();

    userRepository.save(user);

    String accessToken = jwtService.generateToken(user.getUsername());
    String refreshToken = jwtService.generateToken(user.getUsername(), 604800);

    String token = tokenService.createToken(user, TokenType.EMAIL_VERIFICATION);
    Mail mail = mailContextBuilder.buildEmailVerificationMail(user, token);
    mailService.sendMail(mail);

    log.info("User register to system with username:`{}`", user.getUsername());
    return new AuthResponse(user.getUsername(), accessToken, refreshToken);
  }

  public AuthResponse authenticateUser(AuthRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password())
      );
      
      User user = (User) authentication.getPrincipal();
      
      String accessToken = jwtService.generateToken(user.getUsername());
      String refreshToken = jwtService.generateToken(user.getUsername(), 604800);  
      return new AuthResponse(user.getUsername(), accessToken, refreshToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid Username or password");
    }
  }

  @Transactional
  public void verifyEmail(String username, String token) {
    User user = userRepository.findByEmail(username).orElseThrow(
      () -> new IllegalArgumentException("Invalid username")
    );

    if (tokenService.validToken(token, username, TokenType.EMAIL_VERIFICATION)) {
      user.setVerified(true);
      userRepository.save(user);
      log.info("User email verified:`{}`", username);
    }
  }

  @Transactional
  public void resendVerificationToken(String username) {
    User user = userRepository.findByEmail(username).orElseThrow(() -> {
      throw new IllegalArgumentException("Invalid username");
    });

    if (user.isVerified()) {
      throw new IllegalStateException("User already verified");
    }

    String token = tokenService.resendToken(user, TokenType.EMAIL_VERIFICATION);
    Mail mail = mailContextBuilder.buildEmailVerificationMail(user, token);
    mailService.sendMail(mail);
  }

  public AuthResponse refreshToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (token == null || !token.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Unauthorize request");
    }
    
    token = token.substring(7);
    String username = jwtService.getUsername(token);

    UserDetails userDetails = userRepository.findByEmail(username).orElseThrow(
      () -> new IllegalArgumentException("Invalid Token")
    );

    if(!jwtService.validToken(userDetails, token)) {
      new IllegalAccessException("Invalid token");
    }

    String accessToken = jwtService.generateToken(username);
    return new AuthResponse(username, accessToken, token);
  }
}
