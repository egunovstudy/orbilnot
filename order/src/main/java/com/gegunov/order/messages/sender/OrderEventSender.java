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

    private static final String TYPE_ID_HEADER_NAME = "__TYPE_ID__";

    @Value("${order.events.topic}")
    private String topic;
    private static final byte[] ORDER_EVENT_CLASS_NAME = OrderEvent.class.getName().getBytes(StandardCharsets.UTF_8);
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void send(OrderEvent orderEvent) {
        ProducerRecord<UUID, OrderEvent> record = new ProducerRecord<>(topic, orderEvent.getAccountId(), orderEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, ORDER_EVENT_CLASS_NAME);

        kafkaTemplate.send(topic, orderEvent);
    }

}
