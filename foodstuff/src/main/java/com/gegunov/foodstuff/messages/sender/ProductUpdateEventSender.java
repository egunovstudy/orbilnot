package com.gegunov.foodstuff.messages.sender;

import com.gegunov.model.ProductUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ProductUpdateEventSender {

    private static final String TYPE_ID_HEADER_NAME = "__KEY_TYPEID__";

    private static final byte[] PRODUCT_UPDATE_EVENT_CLASS_NAME = ProductUpdateEvent.class.getName()
            .getBytes(StandardCharsets.UTF_8);

    private final KafkaTemplate<String, ProductUpdateEvent> kafkaTemplate;

    @Value("${product.update.status.events.topic}")
    private String productUpdateStatusTopic;

    public void sendProductUpdateEvent(ProductUpdateEvent productUpdateEvent) {
        ProducerRecord<String, ProductUpdateEvent> record = new ProducerRecord<>(productUpdateStatusTopic,
                productUpdateEvent.getName(), productUpdateEvent);
        record.headers().add(TYPE_ID_HEADER_NAME, PRODUCT_UPDATE_EVENT_CLASS_NAME);

        kafkaTemplate.send(record);
    }

}
