package com.example.blog.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.blog.blog.Blog;
import com.example.blog.blog.dto.BlogRequest;
import com.example.blog.blog.dto.BlogResponse;
import com.example.blog.shared.PagedResponse;

@Mapper(componentModel = "spring")
public interface BlogMapper {
  BlogMapper INSTANT = Mappers.getMapper(BlogMapper.class);

  @Mapping(source = "author.fullname", target = "author")
  @Mapping(target = "comments", expression = "java(blog.getComments().size())")
  @Mapping(target = "authorImg", expression = "java(blog.getAuthor().getProfile())")
  BlogResponse blogToBlogResponse(Blog blog);

  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "author", expression = "java(null)")
  @Mapping(target = "createdAt", expression = "java(null)")
  @Mapping(target = "editedAt", expression = "java(null)")
  @Mapping(target = "comments", expression = "java(new java.util.ArrayList<>())")
  Blog blogRequestToBlog(BlogRequest blogRequest);

  @Mapping(target = "prev", expression = "java(page.hasPrevious())")
  @Mapping(target = "next", expression = "java(page.hasNext())")
  @Mapping(target = "content", expression = "java(page.getContent().stream().map(b -> INSTANT.blogToBlogResponse(b)).toList())")
  PagedResponse<BlogResponse> pagetToPageResponse(Page<Blog> page);
}
