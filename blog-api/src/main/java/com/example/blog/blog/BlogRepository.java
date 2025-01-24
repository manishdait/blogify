package com.example.blog.blog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.user.User;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
  List<Blog> findByAuthor(User author);
}
