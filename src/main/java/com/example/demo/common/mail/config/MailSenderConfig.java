package com.example.demo.common.mail.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.demo.constant.FreeMakerProperty.*;

@Configuration
public class MailSenderConfig {

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() {
        var cfg = new freemarker.template.Configuration(new Version(FREEMAKER_VERSION));

        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), BASE_PACKAGE_PATH));
        cfg.setDefaultEncoding(DEFAULT_ENCODING);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg;
    }
}
