package com.gegunov.order.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders", schema = "order_service")
public class Order {

    @Id
    private UUID id;

    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "account_id")
    private UUID accountId;
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE)
    private List<OrderItem> orderItems;

    @Formula("(select sum(oi.price) from order_service.order_items oi where oi.order_id = id)")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

}
