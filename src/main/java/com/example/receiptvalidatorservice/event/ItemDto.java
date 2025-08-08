package com.example.receiptvalidatorservice.event;

public record ItemDto(
        String name,
        Long price,
        Long quantity,
        Long sum,
        Long invoiceType,
        Long invoiceSum,
        Long productType,
        Long paymentType
) {
}
