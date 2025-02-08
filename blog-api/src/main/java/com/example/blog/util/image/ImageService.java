package com.example.blog.util.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;

  @Value("${app.asset-path}")
  private String assetFolder;

  public Image uploadImage(String filename, MultipartFile file) {
    File folder = new File(assetFolder);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    String uploadedFile = file.getOriginalFilename();
    filename = filename + uploadedFile;
    String imagePath = assetFolder + "/" + filename;
    System.out.println(imagePath);

    try {
      Files.copy(file.getInputStream(), Path.of(imagePath), StandardCopyOption.REPLACE_EXISTING);
      Image image = Image.builder()
        .filename(filename)
        .path(imagePath)
        .type(file.getContentType())
        .build();
      return imageRepository.save(image);
    } catch (IOException e) {
      throw new RuntimeException("Fail to upload image");
    }
  }

  public Image getImage(String filename) {
    return imageRepository.findByFilename(filename).orElseThrow();
  }
}
