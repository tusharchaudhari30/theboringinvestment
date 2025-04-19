package com.theboringproject.portfolio_service.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.theboringproject.portfolio_service.client.AuthServiceClient;
import com.theboringproject.portfolio_service.exception.TokenValidationFailedException;
import com.theboringproject.portfolio_service.model.dao.User;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    AuthServiceClient authServiceClient;

    public User validate(String token) {
        try {
            User user = authServiceClient.validateToken(token).getBody();
            log.info("Successfully validated token for user {}", user.getEmail());
            return user;
        } catch (Exception e) {
            log.error("Error validating token for user with token {}: {}", token, e.getMessage(), e);
            throw new TokenValidationFailedException("Invalid token");
        }
    }

}
