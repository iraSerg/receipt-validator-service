package com.example.receiptvalidatorservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, Object message, String messageId, String messageKey) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, messageKey, message);
        record.headers().add("messageId", messageId.getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
    }
}
