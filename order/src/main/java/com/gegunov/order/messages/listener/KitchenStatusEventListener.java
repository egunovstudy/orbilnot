package com.gegunov.order.messages.listener;

import com.gegunov.model.KitchenStatus;
import com.gegunov.model.KitchenStatusEvent;
import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.order.service.OrderService;
import com.gegunov.order.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KitchenStatusEventListener {

    private final OrderService orderService;
    private final ProductStockService productStockService;

    @KafkaListener(topics = { "${kitchen.status.events.topic}" }, groupId = "order-service-consumer")
    public void listen(ConsumerRecord<String, KitchenStatusEvent> record) {
        KitchenStatusEvent kitchenStatusEvent = record.value();
        if (kitchenStatusEvent.getStatus() == KitchenStatus.COOKING_COMPLETED) {
            Order order = orderService.getOrder(kitchenStatusEvent.getAccountId(), kitchenStatusEvent.getOrderNumber());
            orderService.updateOrderStatus(kitchenStatusEvent.getAccountId(), kitchenStatusEvent.getOrderNumber(),
                    OrderStatus.READY);
            productStockService.makeProductStockCooked(order);

        } else if (kitchenStatusEvent.getStatus() == KitchenStatus.CANCELLED) {
            Order order = orderService.getOrder(kitchenStatusEvent.getAccountId(), kitchenStatusEvent.getOrderNumber());
            orderService.updateOrderStatus(kitchenStatusEvent.getAccountId(), kitchenStatusEvent.getOrderNumber(),
                    OrderStatus.CANCELLED);
            productStockService.archiveProductStock(order);
        }
    }
}
