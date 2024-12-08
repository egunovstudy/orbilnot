package com.gegunov.kitchen.service;

import com.gegunov.kitchen.mapping.KitchenOrderMapper;
import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.model.KitchenOrderEvent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KitchenOrderEventHandler {

    private final KitchenOrderService kitchenOrderService;

    public void handleKitchenOrderEvent(KitchenOrderEvent kitchenOrderEvent) {
        KitchenOrderMapper mapper = Mappers.getMapper(KitchenOrderMapper.class);
        KitchenOrder kitchenOrder = mapper.toKitchenOrder(kitchenOrderEvent);
        kitchenOrderService.createKitchenOrder(kitchenOrder);
    }
}
