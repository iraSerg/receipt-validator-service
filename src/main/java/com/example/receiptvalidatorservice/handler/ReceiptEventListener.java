package com.example.receiptvalidatorservice.handler;

import com.example.receiptvalidatorservice.event.ReceiptValidatedEvent;
import com.example.receiptvalidatorservice.exception.NonRetryableException;
import com.example.receiptvalidatorservice.exception.SchemaValidationException;
import com.example.receiptvalidatorservice.persistence.model.InvalidMessage;
import com.example.receiptvalidatorservice.persistence.repository.InvalidMessageRepository;
import com.example.receiptvalidatorservice.producer.MessageProducer;
import com.example.receiptvalidatorservice.usecases.ProcessedEventService;
import com.example.receiptvalidatorservice.usecases.impl.JsonSchemaValidatorService;
import com.example.receiptvalidatorservice.util.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@KafkaListener(topics = Topic.RECEIPT_VALIDATE_REQUEST_TOPIC_EVENTS)
@RequiredArgsConstructor
@Slf4j
public class ReceiptEventListener {
    private final ObjectMapper objectMapper;
    private final ProcessedEventService processedEventService;
    private final JsonSchemaValidatorService validatorService;
    private final InvalidMessageRepository invalidMessageRepository;
    private final MessageProducer messageProducer;

    @KafkaHandler
    @Transactional(transactionManager = "transactionManager")
    public void handle(@Payload String message, @Header("messageId") String messageId) {
        if (messageId == null || messageId.isEmpty()) {
            log.error("MessageId is null or empty");
            throw new NonRetryableException("Message id is null");
        }
        if (processedEventService.isDuplicate(messageId)) {
            log.error("Duplicate receipt event received");
            throw new NonRetryableException("Duplicate receipt event received");
        }
        ReceiptValidatedEvent event = null;
        try {
            validatorService.validate(message);
            log.info("Successfully validate message with id={}", messageId);
            event = objectMapper.readValue(message, ReceiptValidatedEvent.class);
            messageProducer.sendMessage(
                    Topic.RECEIPT_VALIDATE_RESPONSE_TOPIC_EVENTS,
                    event,
                    UUID.randomUUID().toString(),
                    event.user().toString());
        } catch (SchemaValidationException e) {
            log.error("Schema validation error");
            InvalidMessage invalidMessage = new InvalidMessage();
            invalidMessage.setRawMessage(message);
            invalidMessage.setError(e.getMessage());
            invalidMessageRepository.save(invalidMessage);

        } catch (JsonProcessingException e) {
            throw new NonRetryableException(e);
        }


        processedEventService.save(messageId);
        log.info("Successfully saved messageId={}", messageId);

    }
}
