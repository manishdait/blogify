package com.example.blog.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.user.dto.UserRequest;
import com.example.blog.user.dto.UserResponse;
import com.example.blog.util.image.Image;
import com.example.blog.util.image.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final ImageService imageService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username).orElseThrow(
      () -> new UsernameNotFoundException(String.format("User with username: `%s` not found.", username))
    );
  }

  public UserResponse info(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getImage());
  }

  public UserResponse update(UserRequest request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    System.out.println(request);
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    userRepository.save(user);
    return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getImage());
  }

  public UserResponse updateAvtar(MultipartFile imageFile, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Image image = imageService.uploadImage(user.getEmail() + "_avtar", imageFile);

    user.setImage(image);
    userRepository.save(user);

    return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getImage());
  }
}
