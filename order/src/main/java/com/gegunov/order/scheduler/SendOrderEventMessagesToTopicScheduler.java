package com.gegunov.order.scheduler;

import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.order.jpa.repository.OrderRepository;
import com.gegunov.order.mapping.OrderMapper;
import com.gegunov.model.OrderEvent;
import com.gegunov.order.messages.sender.OrderEventSender;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendOrderEventMessagesToTopicScheduler {

    private final OrderRepository orderRepository;
    private final OrderEventSender orderEventSender;

    @Scheduled(fixedDelay = 1000)
    public void processNewOrders() {
        List<Order> newOrders = orderRepository.findByOrderStatus(OrderStatus.NEW);
        for (Order order : newOrders) {
            sendNewOrderEvent(order);
        }
    }

    private void sendNewOrderEvent(Order order) {
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        OrderEvent orderEvent = mapper.toOrderEvent(order);
        orderEventSender.send(orderEvent);
        order.setOrderStatus(OrderStatus.SENT);
        orderRepository.save(order);
    }

}
