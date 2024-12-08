package com.gegunov.billing.messages.sender;

import com.gegunov.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentEventSender {

    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";
    private static final byte[] PAYMENT_EVENT_CLASS_NAME = PaymentEvent.class.getName()
            .getBytes(StandardCharsets.UTF_8);

    @Value("${payment.status.events.topic}")
    private String paymentStatusTopic;
    private final KafkaTemplate<UUID, PaymentEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        ProducerRecord<UUID, PaymentEvent> record = new ProducerRecord<>(paymentStatusTopic, paymentEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, PAYMENT_EVENT_CLASS_NAME);

        kafkaTemplate.send(record);
    }

}
