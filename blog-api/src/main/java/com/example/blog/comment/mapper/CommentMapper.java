package com.example.blog.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.blog.comment.Comment;
import com.example.blog.comment.dto.CommentResponse;
import com.example.blog.shared.PagedResponse;
import com.example.blog.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
  CommentMapper INSTANT = Mappers.getMapper(CommentMapper.class);

  @Mapping(source = "comment.author.fullname", target = "author")
  @Mapping(source = "comment.id", target = "id")
  @Mapping(source = "comment.createdAt", target = "createdAt")
  @Mapping(source = "comment.editedAt", target = "editedAt")
  @Mapping(target = "isOwned", expression = "java(comment.getAuthor().getId() == user.getId())")
  @Mapping(target = "authorImg", expression = "java(comment.getAuthor().getImage() == null? null : \"http://localhost:8080/blog-api/v1/image/\" + comment.getAuthor().getImage().getFilename())")
  CommentResponse commentToCommentResponse(Comment comment, User user);

  @Mapping(target = "prev", expression = "java(page.hasPrevious())")
  @Mapping(target = "next", expression = "java(page.hasNext())")
  @Mapping(target = "content", expression = "java(page.getContent().stream().map(c -> INSTANT.commentToCommentResponse(c, user)).toList())")
  PagedResponse<CommentResponse> pageToPagedResponse(Page<Comment> page, User user);
}