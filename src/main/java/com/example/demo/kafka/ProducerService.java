package com.example.demo.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public record ProducerService(KafkaTemplate<String, Long> kafka) {
    public void send(Long id, String userType) {
        var record = new ProducerRecord<String, Long>("UserCreated", id);
        record.headers().add("User-Type", userType.getBytes(StandardCharsets.UTF_8));
        record.headers().add("User-Id", id.toString().getBytes(StandardCharsets.UTF_8));

        this.kafka.send(record);
    }
}
