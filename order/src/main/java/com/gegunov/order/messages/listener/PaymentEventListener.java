package com.gegunov.order.messages.listener;

import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.model.PaymentEvent;
import com.gegunov.model.PaymentEventType;
import com.gegunov.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OrderService orderService;

    @KafkaListener(topics = { "${payment.events.topic}" }, groupId = "order-service-consumer")
    public void listen(ConsumerRecord<String, PaymentEvent> record) {
        PaymentEvent paymentEvent = record.value();
        if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_CONFIRMED) {
            orderService.updateOrderStatus(paymentEvent.getAccountId(), paymentEvent.getOrderNumber(),
                    OrderStatus.CONFIRMED);
        } else if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_DENIED) {
            orderService.updateOrderStatus(paymentEvent.getAccountId(), paymentEvent.getOrderNumber(),
                    OrderStatus.CANCELLED);
        }
    }
}