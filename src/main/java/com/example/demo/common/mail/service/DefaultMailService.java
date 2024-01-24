package com.example.demo.common.mail.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.example.demo.constant.FreeMakerProperty.DEFAULT_ENCODING;
import static com.example.demo.constant.FreeMakerProperty.FREEMAKER_TEMPLATE_EXTENSION;

@Service
public class DefaultMailService implements MailSender {

    private final JavaMailSender mailSender;
    private final String from;
    private final String fromName;
    private final Configuration templateConfig;

    public DefaultMailService(JavaMailSender mailSender,
                              @Value("${spring.mail.username}") String from,
                              @Value("${spring.mail.from.name}") String fromName,
                              @Qualifier("freemarkerConfiguration") Configuration templateConfig) {
        this.mailSender = mailSender;
        this.from = from;
        this.fromName = fromName;
        this.templateConfig = templateConfig;
    }

    @Override
    public void send(String recipient, String subject, String content, String cc, String bcc)
            throws UnsupportedEncodingException, MessagingException {
        var mimeMessage = this.mailSender.createMimeMessage();
        mimeMessage.setContent(content, "text/html; charset=UTF-8");

        var helper = new MimeMessageHelper(
                mimeMessage,
                false,
                DEFAULT_ENCODING
        );
        helper.setFrom(this.from, this.fromName);
        helper.setTo(recipient);
        helper.setSubject(subject);
        if (cc != null) {
            helper.setCc(cc);
        }
        if (bcc != null) {
            helper.setBcc(bcc);
        }

        this.mailSender.send(mimeMessage);
    }

    @Override
    public void send(String recipient, String subject, Class<?> templateClass, Object model, String cc, String bcc)
            throws IOException, TemplateException, MessagingException {
        var template = this.templateConfig
                .getTemplate(templateClass.getSimpleName().replaceAll("Template", "")
                        + FREEMAKER_TEMPLATE_EXTENSION);
        var writer = new StringWriter();
        var env = template.createProcessingEnvironment(Map.of("model", model), writer);
        env.setOutputEncoding("UTF-8");
        env.process();
        var content = writer.toString();

        this.send(recipient, subject, content, cc, bcc);
    }

    @Override
    public void send(String recipient, String subject, Class<?> templateClass, Object model)
            throws IOException, TemplateException, MessagingException {
        this.send(recipient, subject, templateClass, model, null, null);
    }
}
