package com.theboringproject.authenticationservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtUtilService {
    final Algorithm algorithm;

    public JwtUtilService(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String generateToken(String subject) {
        return JWT.create().withIssuer("boring")
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .withJWTId(UUID.randomUUID()
                        .toString())
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("boring")
                .build();
        return verifier.verify(token).getSubject();
    }

}