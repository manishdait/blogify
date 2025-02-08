package com.example.blog.util.mail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.blog.user.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailContextBuilder {
  private final TemplateEngine templateEngine;

  public Mail buildEmailVerificationMail(User user, String code) {
    Map<String, Object> content = new HashMap<>();  
    content.put("username", user.getFullname());
    content.put("verification_code", code);
    content.put("verification_url", "http://localhost:4200/" + user.getEmail() + "/verify");

    String body = composeBody(MailTemplate.VERIFICATION_TEMPLATE, content);
  
    return new Mail(user.getEmail(), "Verify your account", body);
  }

  private String composeBody(MailTemplate template, Map<String, Object> content) {
    Context context = new Context();
    context.setVariable("content", content);
    return templateEngine.process(template.getTemplate(), context);
  }
}
