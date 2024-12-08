package com.gegunov.kitchen.service;

import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.kitchen.jpa.model.KitchenOrderStatus;
import com.gegunov.kitchen.jpa.repository.KitchenOrderRepository;
import com.gegunov.kitchen.mapping.KitchenOrderMapper;
import com.gegunov.kitchen.messages.sender.KitchenStatusEventSender;
import com.gegunov.model.KitchenStatusEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KitchenOrderService {

    private final KitchenOrderRepository kitchenOrderRepository;
    private final KitchenStatusEventSender kitchenStatusEventSender;

    public void createKitchenOrder(KitchenOrder kitchenOrder) {
        kitchenOrder.setId(UUID.randomUUID());
        kitchenOrder.setStatus(KitchenOrderStatus.NEW);
        kitchenOrderRepository.save(kitchenOrder);
    }

    public void updateKitchenOrder(KitchenOrder kitchenOrder) {
        kitchenOrderRepository.save(kitchenOrder);

    }

    public Iterable<KitchenOrder> getPreparedOrders() {
        return kitchenOrderRepository.findByStatus(KitchenOrderStatus.COOKING_COMPLETED);
    }

    @Transactional
    public void sendKitchenStatusEvent(KitchenOrder kitchenOrder) {
        KitchenStatusEvent kitchenStatusEvent = Mappers.getMapper(KitchenOrderMapper.class)
                .toKitchenStatusEvent(kitchenOrder);
        kitchenStatusEventSender.sendKitchenStatusEvent(kitchenStatusEvent);
        kitchenOrder.setStatus(KitchenOrderStatus.READY);
        kitchenOrderRepository.save(kitchenOrder);
    }
}
