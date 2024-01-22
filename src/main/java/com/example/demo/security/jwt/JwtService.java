package com.example.demo.security.jwt;

import com.example.demo.entity.Admission;

public interface JwtService {
    String create(Admission admission);
}
