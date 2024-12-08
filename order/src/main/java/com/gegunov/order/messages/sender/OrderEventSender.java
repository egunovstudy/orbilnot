package com.gegunov.order.messages.sender;

import com.gegunov.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderEventSender {

    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";
    private static final byte[] BILLING_EVENT_CLASS_NAME = OrderEvent.class.getName().getBytes(StandardCharsets.UTF_8);

    @Value("${order.events.topic}")
    private String billingTopic;

    private final KafkaTemplate<UUID, OrderEvent> kafkaTemplate;

    public void sendOrderToBilling(OrderEvent orderEvent) {
        ProducerRecord<UUID, OrderEvent> record = new ProducerRecord<>(billingTopic, orderEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, BILLING_EVENT_CLASS_NAME);

        kafkaTemplate.send(record);
    }

}
