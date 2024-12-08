package com.gegunov.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class KitchenStatusEvent {
    private UUID accountId;

    private String orderNumber;
    private KitchenStatus status;
}
