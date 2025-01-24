package com.example.blog.mail;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mail {
  private String recipent;
  private String subject;
  private MailTemplate template;
  private Map<String, Object> content;
}
