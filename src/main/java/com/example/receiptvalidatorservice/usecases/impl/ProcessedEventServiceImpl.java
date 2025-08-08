package com.example.receiptvalidatorservice.usecases.impl;

import com.example.receiptvalidatorservice.persistence.model.ProcessedEvent;
import com.example.receiptvalidatorservice.persistence.repository.ProcessedEventRepository;
import com.example.receiptvalidatorservice.usecases.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessedEventServiceImpl implements ProcessedEventService {

    private final ProcessedEventRepository processedEventRepository;

    @Override
    public boolean isDuplicate(String messageId) {
        return processedEventRepository.existsByMessageId(messageId);
    }

    @Override
    public void save(String messageId) {
        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setMessageId(messageId);
        processedEvent.setCreatedAt(LocalDateTime.now());
        processedEventRepository.save(processedEvent);
    }
}

