package com.theboringproject.portfolio_service.websocket;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.theboringproject.portfolio_service.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("UserHandshakeInterceptor: beforeHandshake called");
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = req.getParameter("Authorization");
            log.info("UserHandshakeInterceptor: Token received: {}", token);
            String username = "Portfolio " + authenticationService.validate(token).getEmail();
            if (username != null) {
                Principal userPrincipal = () -> username;
                attributes.put("user", userPrincipal);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
    }
}
