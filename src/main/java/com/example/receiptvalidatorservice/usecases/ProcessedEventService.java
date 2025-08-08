package com.example.receiptvalidatorservice.usecases;

public interface ProcessedEventService {
    boolean isDuplicate(String messageId);

    void save(String messageId);
}
