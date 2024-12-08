package com.gegunov.order.web.model;

import com.gegunov.order.jpa.model.MenuItem;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderAction {
    private UUID id;
    private String orderNumber;
    private UUID accountId;

    private List<MenuItem> orderItems;
}
