package com.example.blog.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.blog.blog.dto.BlogRequest;
import com.example.blog.blog.dto.BlogResponse;
import com.example.blog.blog.mapper.BlogMapper;
import com.example.blog.handler.exception.UnauthorizeAccessException;
import com.example.blog.shared.PagedResponse;
import com.example.blog.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService {
  private final BlogRepository blogRepository;
  private final BlogMapper blogMapper;

  public BlogResponse createBlog(BlogRequest request, Authentication authentication){
    User user = (User) authentication.getPrincipal();
    if (!user.isVerified()) {
      throw new IllegalStateException("User email is not verified");
    }

    Blog blog = blogMapper.blogRequestToBlog(request);
    blog.setAuthor(user);
    
    blogRepository.save(blog);
    return blogMapper.blogToBlogResponse(blog);
  }

  public PagedResponse<BlogResponse> getBlogs(int number, int size){
    Pageable pageable = PageRequest.of(number, size);
    Page<Blog> blogPage =  blogRepository.findAll(pageable);

    return blogMapper.pagetToPageResponse(blogPage);
  }

  public PagedResponse<BlogResponse> getBlogsForAuthor(String author, int number, int size){
    Pageable pageable = PageRequest.of(number, size);
    Page<Blog> blogPage =  blogRepository.findByAuthor(author, pageable);
    
    return blogMapper.pagetToPageResponse(blogPage);
  }

  public BlogResponse getBlog(Integer id){
    Blog blog = blogRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Blog does not found")
    );
    return blogMapper.blogToBlogResponse(blog);
  }

  public BlogResponse updateBlog(Integer id, BlogRequest request, Authentication authentication){
    Blog blog = blogRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Blog does not found")
    );

    User user = (User) authentication.getPrincipal();

    if (!blog.getAuthor().getUsername().equals(user.getUsername())) {
      throw new UnauthorizeAccessException("Operation is forbidden");
    }
    
    blog.setContent(request.content());
    blog.setTitle(request.title());
    blogRepository.save(blog);

    return blogMapper.blogToBlogResponse(blog);
  }

  public void deleteBlog(Integer id, Authentication authentication){
    Blog blog = blogRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Blog does not found")
    );

    User user = (User) authentication.getPrincipal();
    if (!blog.getAuthor().getUsername().equals(user.getUsername())) {
      throw new UnauthorizeAccessException("Operation is forbidden");
    }

    blogRepository.delete(blog);
  }
}
