package com.example.blog.mail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.blog.user.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  private String sender = "springboot@blogapi.com";

  public void sendEmailVerificationMail(User user, String code) {
    Map<String, Object> content = new HashMap<>();  
    content.put("username", user.getFullname());
    content.put("verification_code", code);
    content.put("verification_url", "http://localhost:4200/" + user.getEmail() + "/verify");
  
    Mail mail = new Mail(
      user.getEmail(), 
      "Verify your account",
      MailTemplate.VERIFICATION_TEMPLATE,
      content
    );

    sendMail(mail);
  }

  @Async
  private void sendMail(Mail mail) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
    
    try {
      messageHelper.setTo(mail.getRecipent());
      messageHelper.setSubject(mail.getSubject());
      messageHelper.setFrom(sender);
      messageHelper.setText(composeBody(mail.getTemplate(), mail.getContent()), true);
    } catch (MessagingException e) {
      e.printStackTrace();
    }

    try {
      mailSender.send(mimeMessage);
      log.info("Mail send to `{}`", mail.getRecipent());
    } catch (Exception e) {
      log.error("Error sending mail to `{}`", mail.getRecipent());
    }
  }

  private String composeBody(MailTemplate template, Map<String, Object> content) {
    Context context = new Context();
    context.setVariable("content", content);
    return templateEngine.process(template.getTemplate(), context);
  }
}
