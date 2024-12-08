package com.gegunov.kitchen.scheduler;

import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.kitchen.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendReadyOrdersScheduler {

    private final KitchenOrderService orderService;

    @Scheduled(fixedDelay = 5000)
    public void sendReadyOrders() {
        Iterable<KitchenOrder> readyOrders = orderService.getPreparedOrders();
        for (KitchenOrder order : readyOrders) {
            orderService.sendKitchenStatusEvent(order);
        }
    }

}
