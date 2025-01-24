package com.example.blog.blog;

import java.util.List;

import com.example.blog.comment.Comment;
import com.example.blog.user.User;
import com.example.blog.util.AbsractAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "blog")
public class Blog extends AbsractAuditingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_sequence_generator")
  @SequenceGenerator(name = "blog_sequence_generator", sequenceName = "blog_sequence", initialValue = 101, allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "title")
  private String title;

  @Lob
  @Column(name = "content")
  private String content;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @OneToMany(mappedBy = "blog")
  private List<Comment> comments;
}
