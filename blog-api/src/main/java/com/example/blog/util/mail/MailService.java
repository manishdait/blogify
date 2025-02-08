package com.example.blog.util.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String sender;

  @Async
  public void sendMail(Mail mail) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
    
    try {
      messageHelper.setTo(mail.getRecipent());
      messageHelper.setSubject(mail.getSubject());
      messageHelper.setFrom(sender);
      messageHelper.setText(mail.getBody(), true);
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
}
