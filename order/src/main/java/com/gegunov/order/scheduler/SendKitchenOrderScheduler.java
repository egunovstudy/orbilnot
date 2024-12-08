package com.gegunov.order.scheduler;

import com.gegunov.model.KitchenOrderEvent;
import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.order.jpa.repository.OrderRepository;
import com.gegunov.order.mapping.OrderMapper;
import com.gegunov.order.messages.sender.KitchenOrderEventSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendKitchenOrderScheduler {

    private final OrderRepository orderRepository;
    private final KitchenOrderEventSender kitchenOrderEventSender;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processBillingConfirmedOrders() {
        List<Order> billingConfirmedOrders = orderRepository.findByOrderStatus(OrderStatus.BILLING_CONFIRMED);
        for (Order order : billingConfirmedOrders) {
            sendKitchenOrderEvent(order);
        }
    }

    private void sendKitchenOrderEvent(Order order) {
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        KitchenOrderEvent kitchenEvent = mapper.toKitchenEvent(order);
        kitchenOrderEventSender.sendOrderToKitchen(kitchenEvent);
        order.setOrderStatus(OrderStatus.PREPARING);
        orderRepository.save(order);
    }

}
