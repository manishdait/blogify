package com.example.blog.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.blog.user.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final UserService userService;
  private final JwtService jwtService;

  private final HandlerExceptionResolver handlerExceptionResolver;

  @SuppressWarnings("null")
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return path.startsWith("/blog-api/v1/auth/")
     || (request.getMethod().equals("GET") && path.equals("/blog-api/v1/blog"));
  }

  @SuppressWarnings("null")
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException {
    try {
      String token = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);

        String username = jwtService.getUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);

        if(jwtService.isValidToken(userDetails, token)) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, 
            null, 
            userDetails.getAuthorities()
          );

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
      handlerExceptionResolver.resolveException(request, response, null, e);
    }

    filterChain.doFilter(request, response);
  }
}
