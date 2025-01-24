package com.example.blog.blog;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.blog.auth.AuthService;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogService {
  private final BlogRepository blogRepository;

  private final AuthService authService;

  public BlogResponse createBlog(BlogRequest request){
    Optional<User> _user = authService.getCurrentUser();
    if (!_user.isPresent()) {
      throw new ForbiddenException("User is not authenticated");
    }
    if (!_user.get().isVerified()) {
      throw new ForbiddenException("User is not verified");
    }

    Blog blog = Blog.builder()
      .title(request.title())
      .content(request.content())
      .author(_user.get())
      .comments(List.of())
      .build();
    
    blogRepository.save(blog);
    return new BlogResponse(
      blog.getId(), 
      blog.getTitle(), 
      blog.getContent(), 
      blog.getAuthor().getFullname(),
      blog.getComments().size(),
      blog.getCreatedAt()
    );
  }

  public List<BlogResponse> getBlogs(){
    return blogRepository.findAll().stream()
      .map(
        blog -> new BlogResponse(
          blog.getId(), 
          blog.getTitle(), 
          blog.getContent(), 
          blog.getAuthor().getFullname(),
          blog.getComments().size(),
          blog.getCreatedAt()
        )
      )
      .toList();
  }

  public List<BlogResponse> getBlogsForAuthor(String author){
    return blogRepository.findAll().stream()
      .filter(blog -> blog.getAuthor().getFullname().equals(author))
      .map(
        blog -> new BlogResponse(
          blog.getId(), 
          blog.getTitle(), 
          blog.getContent(), 
          blog.getAuthor().getFullname(),
          blog.getComments().size(),
          blog.getCreatedAt()
        )
      )
      .toList();
  }

  public BlogResponse getBlog(Integer id){
    Blog blog = blogRepository.findById(id).orElseThrow(
      () -> new IllegalArgumentException("Blog does not found")
    );
    return new BlogResponse(
      blog.getId(), 
      blog.getTitle(), 
      blog.getContent(), 
      blog.getAuthor().getFullname(),
      blog.getComments().size(),
      blog.getCreatedAt()
    );
  }

  public BlogResponse updateBlog(Integer id, BlogRequest request){
    Blog blog = blogRepository.findById(id).orElseThrow(
      () -> new IllegalArgumentException("Blog does not found")
    );

    Optional<User> _user = authService.getCurrentUser();
    if (!_user.isPresent() || !_user.get().isVerified() || !blog.getAuthor().getUsername().equals(_user.get().getUsername())) {
      throw new ForbiddenException("Operation is forbidden");
    }
    
    blog.setContent(request.content());
    blog.setTitle(request.title());
    blogRepository.save(blog);

    return new BlogResponse(
      blog.getId(), 
      blog.getTitle(), 
      blog.getContent(), 
      blog.getAuthor().getFullname(),
      blog.getComments().size(),
      blog.getCreatedAt()
    );
  }

  public void deleteBlog(Integer id){
    Blog blog = blogRepository.findById(id).orElseThrow(
      () -> new IllegalArgumentException("Blog does not found")
    );

    Optional<User> _user = authService.getCurrentUser();
    if (!_user.isPresent() || !_user.get().isVerified() || blog.getAuthor().getUsername() != _user.get().getUsername()) {
      throw new ForbiddenException("Operation is forbidden");
    }

    blogRepository.delete(blog);
  }
}
