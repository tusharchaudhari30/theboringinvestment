package com.theboringproject.stock_data_feed.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.theboringproject.stock_data_feed.model.dto.StockPrice;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@DependsOn("createTopic") // Ensure topic is created before sending messages
@Service
public class StockPriceProducer {

    private static final String TOPIC = "stock-prices";
    private static final String[] STOCKS = { "AAPL", "TSLA" };
    private final Random random = new Random();

    private final KafkaTemplate<String, StockPrice> stockPriceKafkaTemplate;

    StockPriceProducer(
            @Qualifier("stockPriceKafkaTemplate") KafkaTemplate<String, StockPrice> stockPriceKafkaTemplate) {
        this.stockPriceKafkaTemplate = stockPriceKafkaTemplate;
    }

    @Scheduled(fixedRate = 5000) // Send data every second
    public void sendStockPriceUpdate() {
        for (String stock : STOCKS) {
            double price = 100 + random.nextDouble() * 50; // Random price
            StockPrice stockPrice = new StockPrice(stock, price);
            stockPriceKafkaTemplate.send(TOPIC, stock, stockPrice);
            log.info("Sent stock price update: {}", stockPrice);
        }
    }
}
