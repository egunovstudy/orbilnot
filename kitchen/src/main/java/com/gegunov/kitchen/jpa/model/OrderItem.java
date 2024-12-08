package com.gegunov.kitchen.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "kitchen_service")
@Getter
@Setter
public class OrderItem {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private KitchenOrder order;
    private String dishName;
    private String details;

}
