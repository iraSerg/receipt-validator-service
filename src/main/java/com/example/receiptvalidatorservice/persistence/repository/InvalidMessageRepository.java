package com.example.receiptvalidatorservice.persistence.repository;

import com.example.receiptvalidatorservice.persistence.model.InvalidMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvalidMessageRepository extends MongoRepository<InvalidMessage, String> {
}
