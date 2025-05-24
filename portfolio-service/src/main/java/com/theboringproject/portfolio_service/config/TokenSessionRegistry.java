package com.theboringproject.portfolio_service.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenSessionRegistry {
    private final Map<String, Set<String>> tokenToSessions = new ConcurrentHashMap<>();

    public void registerSession(String token, String sessionId) {
        tokenToSessions.computeIfAbsent(token, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
    }

    public Set<String> getSessions(String token) {
        return tokenToSessions.getOrDefault(token, Set.of());
    }
}