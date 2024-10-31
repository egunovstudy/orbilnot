package com.gegunov.order.service;

import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.order.jpa.repository.OrderRepository;
import com.gegunov.order.mapping.OrderMapper;
import com.gegunov.order.web.model.OrderAction;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public UUID createOrder(OrderAction orderAction) {
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        Order order = mapper.toOrder(orderAction);
        order.setOrderDate(LocalDateTime.now());
        order.setId(UUID.randomUUID());
        order.setOrderNumber(orderAction.getAccountId() + ":" + order.getId());
        order.setOrderStatus(OrderStatus.NEW);
        orderRepository.save(order);
        return order.getId();
    }

    public void updateOrderAmount(OrderAction orderAction) {
        Order order = orderRepository.findByAccountIdAndOrderNumber(orderAction.getAccountId(),
                orderAction.getOrderNumber()).orElseThrow(EntityNotFoundException::new);
        order.setAmount(orderAction.getAmount());
        orderRepository.save(order);
    }

    public void updateOrderStatus(UUID accountId, String orderNumber, OrderStatus orderStatus) {
        Order order = orderRepository.findByAccountIdAndOrderNumber(accountId, orderNumber)
                .orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    public void deleteOrder(UUID accountId, String orderNumber) {
        Order order = orderRepository.findByAccountIdAndOrderNumber(accountId, orderNumber)
                .orElseThrow(EntityNotFoundException::new);
        orderRepository.delete(order);
    }

    public List<OrderAction> getOrders(UUID accountId) {
        List<Order> orders = orderRepository.findByAccountId(accountId);
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        return mapper.toOrderActionList(orders);
    }

}
