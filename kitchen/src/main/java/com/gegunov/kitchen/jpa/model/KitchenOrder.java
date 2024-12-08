package com.gegunov.kitchen.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kitchen_orders", schema = "kitchen_service")
@Getter
@Setter
public class KitchenOrder {
    @Id
    private UUID id;
    private UUID accountId;
    private String orderNumber;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
    @Enumerated(EnumType.STRING)
    private KitchenOrderStatus status;
}
