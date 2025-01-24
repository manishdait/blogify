package com.example.blog.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.blog.auth.AuthService;
import com.example.blog.blog.Blog;
import com.example.blog.blog.BlogRepository;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final BlogRepository blogRepository;

  private final AuthService authService;

  public CommentResponse createComment(CommentRequest request) {
    Optional<User> _user = authService.getCurrentUser();
    if (!_user.isPresent() || !_user.get().isVerified()) {
      throw new ForbiddenException("Operation is forbidden");
    }

    Blog blog = blogRepository.findById(request.blogId()).orElseThrow(
      () -> new IllegalArgumentException("Blog does not found")
    );

    Comment comment = Comment.builder()
      .message(request.message())
      .author(_user.get())
      .blog(blog)
      .build();
    commentRepository.save(comment);

    return new CommentResponse(comment.getId(), comment.getMessage(), comment.getAuthor().getFullname(), comment.getCreatedAt());
  }

  public List<CommentResponse> getCommentForBlog(Integer blogId) {
    Blog blog = blogRepository.findById(blogId) .orElseThrow(
      () -> new IllegalArgumentException("Blog does not found")
    );

    return commentRepository.findByBlog(blog).stream()
      .map(
        comment -> new CommentResponse(comment.getId(), comment.getMessage(), comment.getAuthor().getFullname(), comment.getCreatedAt())
      )
      .toList();
  }

  public CommentResponse updateComment(Integer id, CommentRequest request) {
    Comment comment = commentRepository.findById(id).orElseThrow(
      () -> new IllegalArgumentException("Comment not found")
    );

    Optional<User> _user = authService.getCurrentUser();
    if (!_user.isPresent() || !_user.get().isVerified() || !_user.get().getUsername().equals(comment.getAuthor().getUsername())) {
      throw new ForbiddenException("Operation is forbidden");
    }

    comment.setMessage(comment.getMessage());
    commentRepository.save(comment);

    return new CommentResponse(comment.getId(), comment.getMessage(), comment.getAuthor().getFullname(), comment.getCreatedAt());
  }

  public void deleteComment(Integer id) {
    Comment comment = commentRepository.findById(id).orElseThrow(
      () -> new IllegalArgumentException("Comment not found")
    );

    Optional<User> _user = authService.getCurrentUser();
    if (!_user.isPresent() || !_user.get().isVerified() || !_user.get().getUsername().equals(comment.getAuthor().getUsername())) {
      throw new ForbiddenException("Operation is forbidden");
    }

    commentRepository.delete(comment);
  }
}
