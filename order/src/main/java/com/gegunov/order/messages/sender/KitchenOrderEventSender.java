package com.gegunov.order.messages.sender;

import com.gegunov.model.KitchenOrderEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KitchenOrderEventSender {

    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";
    private static final byte[] KITCHEN_ORDER_CLASS_NAME = KitchenOrderEvent.class.getName()
            .getBytes(StandardCharsets.UTF_8);

    @Value("${kitchen.order.events.topic}")
    private String kitchenTopic;

    private final KafkaTemplate<UUID, KitchenOrderEvent> kafkaTemplate;

    public void sendOrderToKitchen(KitchenOrderEvent kitchenOrderEvent) {
        ProducerRecord<UUID, KitchenOrderEvent> record = new ProducerRecord<>(kitchenTopic, kitchenOrderEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, KITCHEN_ORDER_CLASS_NAME);

        kafkaTemplate.send(record);
    }

}
