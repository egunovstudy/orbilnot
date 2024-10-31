package com.gegunov.order.web.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderAction {
    private UUID id;
    private String orderNumber;

    private BigDecimal amount;
    private UUID accountId;
}
