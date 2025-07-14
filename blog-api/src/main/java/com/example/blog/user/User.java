package com.example.blog.user;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.blog.shared.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@ToString
@Table(name = "app_user")
public class User extends AbstractAuditingEntity implements UserDetails, Principal {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence_generator")
  @SequenceGenerator(name = "user_sequence_generator", sequenceName = "user_sequence", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "first_name")
  @JsonProperty("first_name")
  private String firstName;

  @Column(name = "last_name")
  @JsonProperty("last_name")
  private String lastName;

  @Column(name= "email", unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "verified")
  private boolean verified;

  @Column(name = "profile")
  private String profile;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_User"));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  public String getFullname() {
    return firstName + " " + lastName;
  }

  @Override
  public String getName() {
    return this.email;
  }
}
