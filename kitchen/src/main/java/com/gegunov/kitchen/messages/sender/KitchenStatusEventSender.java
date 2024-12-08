package com.gegunov.kitchen.messages.sender;

import com.gegunov.model.KitchenStatusEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KitchenStatusEventSender {
    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";

    private static final byte[] KITCHEN_STATUS_EVENT_CLASS_NAME = KitchenStatusEvent.class.getName()
            .getBytes(StandardCharsets.UTF_8);

    @Value("${kitchen.status.events.topic}")
    private String kitchenStatusEventTopic;

    private final KafkaTemplate<UUID, KitchenStatusEvent> kafkaTemplate;

    public void sendKitchenStatusEvent(KitchenStatusEvent reservationEvent) {
        ProducerRecord<UUID, KitchenStatusEvent> record = new ProducerRecord<>(kitchenStatusEventTopic,
                reservationEvent.getAccountId(), reservationEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, KITCHEN_STATUS_EVENT_CLASS_NAME);

        kafkaTemplate.send(record);
    }
}
