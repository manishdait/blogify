package com.example.blog.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.user.dto.UserRequest;
import com.example.blog.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final S3Client s3Client;

  @Value("${minio.bucket}")
  private String bucket;

  @Value("${minio.end-point}")
  private String endPoint;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username).orElseThrow(
      () -> new UsernameNotFoundException("User with username: `%s` not found.".formatted(username))
    );
  }

  public UserResponse info(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfile());
  }

  public UserResponse update(UserRequest request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    System.out.println(request);
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    userRepository.save(user);
    return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfile());
  }

  public UserResponse updateAvtar(MultipartFile file, Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    try {
      String imageUrl = uploadFile(file, "users/%s/profile.%s".formatted(user.getUsername(), file.getContentType()));
      user.setProfile(imageUrl);
      
      userRepository.save(user);
      return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfile());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Fail to update image");
    }
  }

  private String uploadFile(MultipartFile file, String key) throws Exception {
    PutObjectRequest request = PutObjectRequest.builder()
      .bucket(bucket)
      .key(key)
      .contentType(file.getContentType())
      .contentLength(file.getSize())
      .acl(ObjectCannedACL.PUBLIC_READ)
      .build();

    s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

    String url = String.format("%s/%s/%s", endPoint, bucket, key);
    return url;
  }
}
