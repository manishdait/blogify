package com.example.blog.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.blog.user.role.Role;
import com.example.blog.util.AbsractAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "blog_user")
public class User extends AbsractAuditingEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence_generator")
  @SequenceGenerator(name = "user_sequence_generator", sequenceName = "user_sequence", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name= "email", unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "verified")
  private boolean verified;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", 
    joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "user_id")})
  private List<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
      .map(role -> new SimpleGrantedAuthority(role.getRole()))
      .toList();
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  public String getFullname() {
    return firstName + " " + lastName;
  }
}
