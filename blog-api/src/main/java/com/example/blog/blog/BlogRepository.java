package com.example.blog.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
  @Query("select b from Blog b  where b.author.email = :author")
  Page<Blog> findByAuthor(@Param("author") String author, Pageable pageable);
}
