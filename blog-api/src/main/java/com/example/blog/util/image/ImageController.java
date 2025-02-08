package com.example.blog.util.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog-api/v1/image")
@Hidden
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;

  @GetMapping("/{filename}")
  public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
    Image image = imageService.getImage(filename);

    try {
      byte[] bytes = Files.readAllBytes(Path.of(image.getPath()));
      return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(bytes);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Fail to retrive image");
    }
  }
}
