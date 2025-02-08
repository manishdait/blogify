package com.example.blog.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.user.dto.UserRequest;
import com.example.blog.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "API to interact with user info")
public class UserController {
  private final UserService userService;

  @GetMapping("/info")
  @Operation(summary = "Get the use info for username")
  @SecurityRequirement(name = "accessToken")
  public ResponseEntity<UserResponse> info(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.info(authentication));
  }

  @PutMapping("/update")
  public ResponseEntity<UserResponse> updateAvtar(@RequestBody UserRequest request, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(request, authentication));
  }

  @PutMapping("/update-avtar")
  public ResponseEntity<UserResponse> updateAvtar(@RequestPart MultipartFile image, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateAvtar(image, authentication));
  }
}
