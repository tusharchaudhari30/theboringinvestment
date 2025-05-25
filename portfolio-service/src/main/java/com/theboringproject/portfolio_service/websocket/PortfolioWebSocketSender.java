package com.theboringproject.portfolio_service.websocket;

import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.theboringproject.portfolio_service.config.TokenSessionRegistry;
import com.theboringproject.portfolio_service.model.dto.Portfolio;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PortfolioWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;
    private final TokenSessionRegistry tokenSessionRegistry;

    public PortfolioWebSocketSender(SimpMessagingTemplate messagingTemplate,
            TokenSessionRegistry tokenSessionRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.tokenSessionRegistry = tokenSessionRegistry;
    }

    public void sendToUser(String email, Portfolio portfolio) {
        messagingTemplate.convertAndSendToUser(email, "/queue/portfolio", portfolio);
    }
}
