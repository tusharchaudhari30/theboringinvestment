package com.theboringproject.stock_data_feed.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTopicConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Value("${spring.kafka.topic.partitions:3}") // Default 3 partitions
    private int partitions;

    @Value("${spring.kafka.topic.replication-factor:1}") // Default replication factor
    private short replicationFactor;

    @Value("${spring.kafka.topic.retention-ms:5000}") // Default retention: 5s
    private String retentionMs;

    @Value("${spring.kafka.topic.cleanup-policy:delete}") // Default delete policy
    private String cleanupPolicy;

    @Value("${spring.kafka.topic.segment-bytes:1048576}") // 1MB segments
    private String segmentBytes;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public AdminClient adminClient(KafkaAdmin kafkaAdmin) {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }

    @Bean
    public NewTopic createTopic() {
        LOGGER.info("Creating Kafka topic: {}", topicName);
        try {
            return new NewTopic(topicName, partitions, replicationFactor)
                    .configs(Map.of(
                            TopicConfig.RETENTION_MS_CONFIG, retentionMs,
                            TopicConfig.CLEANUP_POLICY_CONFIG, cleanupPolicy,
                            TopicConfig.SEGMENT_BYTES_CONFIG, segmentBytes));
        } catch (Exception e) {
            LOGGER.error("Failed to create Kafka topic", e);
            throw e;
        }
    }
}
