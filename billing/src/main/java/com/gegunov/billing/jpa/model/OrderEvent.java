package com.gegunov.billing.jpa.model;

import com.gegunov.model.OrderEventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "order_events", schema = "billing")
public class OrderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing.order_events_id_seq")
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "event_type")
    private OrderEventType eventType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "account_id")
    private UUID accountId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_event_state")
    private OrderEventState orderEventState;

    @Column(name = "error_message")
    private String errorMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderEvent orderEvent = (OrderEvent) o;
        return Objects.equals(id, orderEvent.id)
                && orderNumber.equals(orderEvent.orderNumber)
                && eventType == orderEvent.eventType
                && amount.equals(orderEvent.amount)
                && accountId.equals(orderEvent.accountId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + orderNumber.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + accountId.hashCode();
        return result;
    }
}
