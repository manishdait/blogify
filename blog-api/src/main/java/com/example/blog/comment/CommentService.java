package com.example.blog.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.blog.blog.Blog;
import com.example.blog.blog.BlogRepository;
import com.example.blog.comment.dto.CommentRequest;
import com.example.blog.comment.dto.CommentResponse;
import com.example.blog.comment.mapper.CommentMapper;
import com.example.blog.handler.exception.UnauthorizeAccessException;
import com.example.blog.shared.PagedResponse;
import com.example.blog.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  private final BlogRepository blogRepository;

  public CommentResponse createComment(CommentRequest request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    if (!user.isVerified()) {
      throw new IllegalStateException("User account is not verified");
    }

    Blog blog = blogRepository.findById(request.blogId()).orElseThrow(
      () -> new EntityNotFoundException("Blog does not found")
    );

    Comment comment = Comment.builder()
      .message(request.message())
      .author(user)
      .blog(blog)
      .build();
    commentRepository.save(comment);

    return commentMapper.commentToCommentResponse(comment, user);
  }

  public PagedResponse<CommentResponse> getCommentForBlog(Integer blogId, Authentication authentication, int number, int size) {
    User user = (User) authentication.getPrincipal();

    Pageable pageable = PageRequest.of(number, size);
    Page<Comment> commentPage = commentRepository.findByBlog(blogId, pageable);

    return commentMapper.pageToPagedResponse(commentPage, user);
  }

  public CommentResponse updateComment(Integer id, CommentRequest request, Authentication authentication) {
    Comment comment = commentRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Comment not found")
    );

    User user = (User) authentication.getPrincipal();
    if (!user.getUsername().equals(comment.getAuthor().getUsername())) {
      throw new UnauthorizeAccessException("Operation is forbidden");
    }

    comment.setMessage(request.message());
    commentRepository.save(comment);

    return commentMapper.commentToCommentResponse(comment, user);
  }

  public void deleteComment(Integer id, Authentication authentication) {
    Comment comment = commentRepository.findById(id).orElseThrow(
      () -> new EntityNotFoundException("Comment not found")
    );

    User user = (User) authentication.getPrincipal();
    if (!user.getUsername().equals(comment.getAuthor().getUsername())) {
      throw new UnauthorizeAccessException("Operation is forbidden");
    }

    commentRepository.delete(comment);
  }
}
