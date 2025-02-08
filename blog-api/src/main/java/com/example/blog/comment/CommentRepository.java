package com.example.blog.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  @Query("select c from Comment c where c.blog.id = :blogId")
  Page<Comment> findByBlog(@Param("blogId") Integer blogId, Pageable pageable);
}
