package com.theboringproject.portfolio_service.service;

import com.theboringproject.portfolio_service.client.AuthServiceClient;
import com.theboringproject.portfolio_service.exception.TokenValidationFailedException;
import com.theboringproject.portfolio_service.model.dao.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthServiceClient authServiceClient;

    public User validate(String token) {
        User user = authServiceClient.validateToken(token).getBody();
        if (user == null) {
            log.error("Token validation failed");
            throw new TokenValidationFailedException("Token validation failed");
        }
        log.info("Token validation successful for user {}", user.getEmail());
        return user;
    }
}
