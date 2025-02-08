package com.example.blog.util.image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> { 
  Optional<Image> findByFilename(String name);
}
