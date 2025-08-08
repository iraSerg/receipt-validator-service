package com.example.receiptvalidatorservice.persistence.repository;

import com.example.receiptvalidatorservice.persistence.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> {
    boolean existsByMessageId(String id);
}
