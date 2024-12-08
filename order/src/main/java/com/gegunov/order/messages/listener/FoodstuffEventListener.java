package com.gegunov.order.messages.listener;

import com.gegunov.model.ProductUpdateEvent;
import com.gegunov.model.ReservationStatusEvent;
import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.order.jpa.model.Product;
import com.gegunov.order.jpa.repository.ProductRepository;
import com.gegunov.order.service.OrderService;
import com.gegunov.order.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FoodstuffEventListener {

    private final OrderService orderService;
    private final ProductStockService productStockService;
    private final ProductRepository productRepository;

    @KafkaListener(topics = { "${reservation.status.events.topic}" }, groupId = "order-service-consumer")
    public void listenReservationStatusEvent(ConsumerRecord<String, ReservationStatusEvent> record) {
        ReservationStatusEvent event = record.value();
        if (event.getState() == ReservationStatusEvent.State.SUCCESS) {
            orderService.updateOrderStatus(event.getAccountId(), event.getOrderNumber(),
                    OrderStatus.RESERVATION_CONFIRMED);
        } else if (event.getState() == ReservationStatusEvent.State.FAILED) {
            Order order = orderService.getOrder(event.getAccountId(), event.getOrderNumber());
            orderService.updateOrderStatus(event.getAccountId(), event.getOrderNumber(), OrderStatus.CANCELLED);
            productStockService.archiveProductStock(order);

        }
    }

    @KafkaListener(topics = { "${product.update.status.events.topic}" }, groupId = "order-service-consumer")
    public void listenProductUpdateEvent(ConsumerRecord<String, ProductUpdateEvent> record) {
        ProductUpdateEvent event = record.value();
        Product product = productRepository.findByCode(event.getCode());
        if(product == null) {
            product = new Product();
            product.setCode(event.getCode());
            product.setId(UUID.randomUUID());
            product.setName(event.getName());
            productRepository.save(product);
        }
        productStockService.createAvailableProductStock(product, event);
    }

}
