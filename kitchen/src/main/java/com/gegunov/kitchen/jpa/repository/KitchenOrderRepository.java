package com.gegunov.kitchen.jpa.repository;

import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.kitchen.jpa.model.KitchenOrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface KitchenOrderRepository extends CrudRepository<KitchenOrder, UUID> {
    List<KitchenOrder> findByStatus(KitchenOrderStatus status);
}
