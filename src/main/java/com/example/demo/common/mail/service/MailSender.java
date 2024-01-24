package com.example.demo.common.mail.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MailSender {
    void send(String recipient, String subject, Class<?> templateClass, Object model)
            throws IOException, TemplateException, MessagingException;

    void send(String recipient, String subject, String content, String cc, String bcc)
            throws MessagingException, UnsupportedEncodingException;

    void send(String recipient, String subject, Class<?> templateClass, Object model, String cc, String bcc)
            throws IOException, TemplateException, MessagingException;
}
