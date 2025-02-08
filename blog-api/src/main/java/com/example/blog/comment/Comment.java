package com.example.blog.comment;

import com.example.blog.blog.Blog;
import com.example.blog.shared.AbstractAuditingEntity;
import com.example.blog.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "comment")
public class Comment extends AbstractAuditingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence_generator")
  @SequenceGenerator(name = "comment_sequence_generator", sequenceName = "comment_sequence", initialValue = 101, allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "message")
  private String message;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "blog_id")
  private Blog blog;
}
