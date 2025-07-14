package com.example.blog.util.mail;

import lombok.Getter;

public enum MailTemplate {
  VERIFICATION_TEMPLATE("otp_verification");

  @Getter
  private final String template;

  MailTemplate(String template) {
    this.template = template;
  }
}
