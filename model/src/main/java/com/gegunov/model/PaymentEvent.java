package com.gegunov.model;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentEvent {

    private UUID accountId;
    private String orderNumber;
    private PaymentEventType paymentEventType;
    private String description;
}
