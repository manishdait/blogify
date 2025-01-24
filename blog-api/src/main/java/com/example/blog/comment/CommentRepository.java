package com.example.blog.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blog.blog.Blog;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  List<Comment> findByBlog(Blog blog);
}
