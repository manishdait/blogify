package com.example.blog.user.domain.role;

import java.util.List;

import com.example.blog.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence_generator")
  @SequenceGenerator(name = "role_sequence_generator", sequenceName = "role_sequence", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "role", unique = true)
  private String role;

  @JsonIgnore
  @ManyToMany(mappedBy = "roles")
  private List<User> users;
}
