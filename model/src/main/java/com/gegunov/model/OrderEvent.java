package com.gegunov.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderEvent {
    private OrderEventType eventType;
    private UUID accountId;
    private String orderNumber;
    private BigDecimal amount;
}
