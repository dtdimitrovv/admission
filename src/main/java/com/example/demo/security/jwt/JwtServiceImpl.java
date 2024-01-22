package com.example.demo.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import com.example.demo.entity.Admission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.demo.constant.TokenAttribute.ROLES_CLAIM;
import static com.example.demo.constant.TokenAttribute.USER_ID_CLAIM;

@Service
public class JwtServiceImpl implements JwtService {

    private static final int EXPIRATION = 60 * 60 * 24;

    private final Algorithm algorithm;

    private final Verification verification;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verification = JWT.require(this.algorithm);
    }

    @Override
    public String create(Admission admission) {
        return JWT.create()
                .withClaim(USER_ID_CLAIM, admission.getId())
                .withClaim(ROLES_CLAIM, admission.getRoles()
                        .stream()
                        .map(String::valueOf)
                        .toList()
                )
                .withExpiresAt(Instant.from(Instant.now().plus(EXPIRATION, ChronoUnit.SECONDS)))
                .sign(this.algorithm);
    }
}
