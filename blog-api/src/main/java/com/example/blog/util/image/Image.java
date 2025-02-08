package com.example.blog.util.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "image")
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq_generator")
  @SequenceGenerator(name = "image_seq_generator", sequenceName = "image_seq", allocationSize = 1, initialValue = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "path")
  private String path;

  @Column(name = "name", unique = true)
  private String filename;

  @Column(name = "type")
  private String type;
}
