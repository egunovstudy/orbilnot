package com.gegunov.billing.messages.sender;

import com.gegunov.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventSender {

    @Value("${payment.events.topic}")
    private String paymentTopic;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        kafkaTemplate.send(paymentTopic, paymentEvent);
    }

}
