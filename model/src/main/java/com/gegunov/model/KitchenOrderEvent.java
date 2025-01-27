package com.gegunov.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class KitchenOrderEvent {
    private UUID accountId;
    private String orderNumber;
    private List<OrderItemDTO> orderItems;
}
