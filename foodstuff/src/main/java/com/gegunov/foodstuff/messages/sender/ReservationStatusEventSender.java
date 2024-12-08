package com.gegunov.foodstuff.messages.sender;

import com.gegunov.model.ReservationStatusEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationStatusEventSender {

    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";
    private static final byte[] RESERVATION_STATUS_EVENT_CLASS_NAME = ReservationStatusEvent.class.getName()
            .getBytes(StandardCharsets.UTF_8);

    private final KafkaTemplate<UUID, ReservationStatusEvent> kafkaTemplate;

    @Value("${reservation.status.events.topic}")
    private String reservationStatusEventsTopic;

    public void sendReservationStatus(ReservationStatusEvent eventStatus) {
        ProducerRecord<UUID, ReservationStatusEvent> record = new ProducerRecord<>(reservationStatusEventsTopic, eventStatus);
        record.headers().add(TYPE_ID_HEADER_NAME, RESERVATION_STATUS_EVENT_CLASS_NAME);

        kafkaTemplate.send(record);
    }

}
