package com.gegunov.order.jpa.repository;

import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Optional<Order> findByAccountIdAndOrderNumber(UUID accountId, String id);

    List<Order> findByAccountId(UUID accountId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
