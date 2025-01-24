package com.example.blog.user.token;

import java.time.Instant;

import com.example.blog.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "token")
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_sequence_generator")
  @SequenceGenerator(name = "token_sequence_generator", sequenceName = "token_sequence", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "code", unique = true)
  private String code;

  @Column(name = "expiration")
  private Instant expiration;

  @Column(name = "verified")
  private boolean verified;
  
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
