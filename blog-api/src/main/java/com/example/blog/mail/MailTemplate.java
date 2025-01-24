package com.example.blog.mail;

import lombok.Getter;

public enum MailTemplate {
  VERIFICATION_TEMPLATE("email_verification");

  @Getter
  private final String template;

  MailTemplate(String template) {
    this.template = template;
  }
}
