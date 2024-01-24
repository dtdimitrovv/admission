package com.example.demo.common.config.mvc;

import com.example.demo.security.interceptor.HasRoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebMvcConfigurer implements WebMvcConfigurer {

    private final HasRoleInterceptor hasRoleInterceptor;

    public ApplicationWebMvcConfigurer(HasRoleInterceptor hasRoleInterceptor) {
        this.hasRoleInterceptor = hasRoleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hasRoleInterceptor);
    }
}
