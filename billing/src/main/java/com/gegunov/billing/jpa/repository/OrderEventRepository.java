package com.gegunov.billing.jpa.repository;

import com.gegunov.billing.jpa.model.OrderEvent;
import com.gegunov.billing.jpa.model.OrderEventState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderEventRepository extends CrudRepository<OrderEvent, Long> {

    List<OrderEvent> findByOrderEventState(OrderEventState orderEventState);

    List<OrderEvent> findByOrderNumber(String orderNumber);
}
