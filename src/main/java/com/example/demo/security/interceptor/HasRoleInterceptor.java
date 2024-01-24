package com.example.demo.security.interceptor;

import com.example.authorizationValidator.security.AuthenticatedUser;
import com.example.demo.entity.Admission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo.constant.TokenAttribute.ROLE_PREFIX;

@Component
public class HasRoleInterceptor implements HandlerInterceptor {

    private final AuthenticatedUser<Admission> authenticatedUser;

    public HasRoleInterceptor(AuthenticatedUser<Admission> authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        var classAnnotation = Optional.of(handlerMethod.getMethod())
                .map(Method::getDeclaringClass)
                .map(declaringClass -> declaringClass.getAnnotation(HasRole.class))
                .orElse(null);
        var methodAnnotation = handlerMethod.getMethodAnnotation(HasRole.class);

        if (classAnnotation == null && methodAnnotation == null) {
            return true;
        }

        var annotationValue = Objects.requireNonNullElse(methodAnnotation, classAnnotation)
                .value();

        return Optional.ofNullable(authenticatedUser
                        .getValue())
                .map(Admission::getRoles)
                .stream()
                .flatMap(Collection::stream)
                .map(role -> ROLE_PREFIX + role.name())
                .filter(name -> name.equals(annotationValue))
                .findFirst()
                .map(s -> Boolean.TRUE)
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    return Boolean.FALSE;
                });
    }
}
