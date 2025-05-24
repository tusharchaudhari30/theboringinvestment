package com.theboringproject.portfolio_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theboringproject.portfolio_service.model.dto.Portfolio;
import com.theboringproject.portfolio_service.model.dto.StockPrice;
import com.theboringproject.portfolio_service.websocket.PortfolioWebSocketSender;

import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockPriceConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RedisTemplate<String, Portfolio> portfolioRedisTemplate;

    private final PortfolioService portfolioService;

    private final PortfolioWebSocketSender webSocketSender;

    public StockPriceConsumer(RedisTemplate<String, Portfolio> portfolioRedisTemplate,
            PortfolioService portfolioService, PortfolioWebSocketSender webSocketSender) {
        this.portfolioService = portfolioService;
        this.portfolioRedisTemplate = portfolioRedisTemplate;
        this.webSocketSender = webSocketSender;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            StockPrice stockPrice = objectMapper.readValue(record.value(), StockPrice.class);
            // Iterate Redis to find users whose portfolio includes the updated stock
            Set<String> keys = portfolioRedisTemplate.keys("Bearer *");
            for (String token : keys) {
                Portfolio portfolio = portfolioRedisTemplate.opsForValue().get(token);
                if (portfolio != null && portfolio.getStockList().stream()
                        .anyMatch(stock -> stock.getTicker().equalsIgnoreCase(stockPrice.getStockSymbol()))) {
                    Portfolio updated = portfolioService.updateStockPrice(token, portfolio, stockPrice.getStockSymbol(),
                            stockPrice.getPrice());
                    webSocketSender.sendToUser(token, updated);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
