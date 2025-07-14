package com.example.blog.token;

import java.time.Instant;

import com.example.blog.shared.AbstractAuditingEntity;
import com.example.blog.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "token")
public class Token extends AbstractAuditingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_sequence_generator")
  @SequenceGenerator(name = "token_sequence_generator", sequenceName = "token_sequence", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "token", unique = true)
  private String token;

  @Column(name = "expiration")
  private Instant expiration;  

  @Enumerated(value = EnumType.STRING)
  @Column(name = "type")
  private TokenType type;

  @Column(name = "used")
  private boolean used;
  
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
