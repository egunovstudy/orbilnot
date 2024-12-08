package com.gegunov.order.messages.sender;

import com.gegunov.model.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationEventSender {

    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";

    @Value("${reservation.events.topic}")
    private String foodstuffTopic;

    private static final byte[] RESERVATION_EVENT_CLASS_NAME = ReservationEvent.class.getName()
            .getBytes(StandardCharsets.UTF_8);

    private final KafkaTemplate<UUID, ReservationEvent> kafkaTemplate;

    public void sendReservationEvent(ReservationEvent reservationEvent) {
        ProducerRecord<UUID, ReservationEvent> record = new ProducerRecord<>(foodstuffTopic, reservationEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, RESERVATION_EVENT_CLASS_NAME);

        kafkaTemplate.send(foodstuffTopic, reservationEvent);
    }

}
