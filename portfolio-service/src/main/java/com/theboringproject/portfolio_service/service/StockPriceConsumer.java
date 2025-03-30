package com.theboringproject.portfolio_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theboringproject.portfolio_service.model.dto.StockPrice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockPriceConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            StockPrice stockPrice = objectMapper.readValue(record.value(), StockPrice.class);
            System.out.println("📥 Received: " + stockPrice);
        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + e.getMessage());
        }
    }
}
