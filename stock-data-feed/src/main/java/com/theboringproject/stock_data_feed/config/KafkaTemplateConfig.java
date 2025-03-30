package com.theboringproject.stock_data_feed.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.theboringproject.stock_data_feed.model.dto.StockPrice;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTemplateConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Default Producer Factory - Normal settings
     */
    @Bean
    public ProducerFactory<String, String> defaultProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // Default "all" acks
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Producer Factory for stock-prices topic (acks=0, retries=0)
     */
    @Bean
    public ProducerFactory<String, StockPrice> stockPriceProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "0"); // No acknowledgment
        configProps.put(ProducerConfig.RETRIES_CONFIG, 0); // No retries
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Default KafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, String> defaultKafkaTemplate() {
        return new KafkaTemplate<>(defaultProducerFactory());
    }

    /**
     * KafkaTemplate for stock-prices topic
     */
    @Bean
    public KafkaTemplate<String, StockPrice> stockPriceKafkaTemplate() {
        return new KafkaTemplate<>(stockPriceProducerFactory());
    }
}
