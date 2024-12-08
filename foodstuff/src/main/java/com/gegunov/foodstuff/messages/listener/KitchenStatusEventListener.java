package com.gegunov.foodstuff.messages.listener;

import com.gegunov.foodstuff.service.KitchenStatusEventHandler;
import com.gegunov.model.KitchenStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class KitchenStatusEventListener {

    private final KitchenStatusEventHandler kitchenStatusEventHandler;

    @KafkaListener(topics = { "${kitchen.status.events.topic}" }, groupId = "foodstuff-service-consumer")
    public void listen(KitchenStatusEvent kitchenStatusEvent) {
        kitchenStatusEventHandler.handleKitchenEvent(kitchenStatusEvent);
    }
}