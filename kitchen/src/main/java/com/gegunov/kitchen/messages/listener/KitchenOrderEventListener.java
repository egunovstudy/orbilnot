package com.gegunov.kitchen.messages.listener;

import com.gegunov.kitchen.service.KitchenOrderEventHandler;
import com.gegunov.model.KitchenOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KitchenOrderEventListener {

    private final KitchenOrderEventHandler handler;

    @KafkaListener(topics = {"${kitchen.order.events.topic}"}, groupId = "kitchen-service-consumer")
    public void listenKitchenOrderEvent(KitchenOrderEvent kitchenOrderEvent) {
        handler.handleKitchenOrderEvent(kitchenOrderEvent);
    }

}
