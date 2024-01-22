package com.example.demo.security.filter;

import com.example.demo.repository.AdmissionRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends BasicAuthenticationFilter {
    private final AdmissionRepository admissionRepository;

    public JwtFilter(
            AuthenticationManager authenticationManager,
            AdmissionRepository admissionRepository
    ) {
        super(authenticationManager);
        this.admissionRepository = admissionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        var customAuthorizationIdHeader = request.getHeader("X-User-Id");

        if (customAuthorizationIdHeader != null) {
            this.admissionRepository.findById(Long.parseLong(customAuthorizationIdHeader))
                    .ifPresentOrElse(admission -> SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            admission,
                                            null,
                                            admission.getRoles()
                                                    .stream()
                                                    .map(role -> "ROLE_" + role)
                                                    .map(SimpleGrantedAuthority::new)
                                                    .collect(Collectors.toList())
                                    )
                            ), () -> {
                        throw new RuntimeException();
                    });
        }

        chain.doFilter(request, response);
    }
}
