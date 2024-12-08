package com.gegunov.billing.messages.listener;

import com.gegunov.billing.jpa.model.OrderEventState;
import com.gegunov.billing.jpa.repository.OrderEventRepository;
import com.gegunov.billing.mapping.OrderEventMapper;
import com.gegunov.billing.messages.validation.OrderEventValidator;
import com.gegunov.billing.messages.validation.exceptions.OrderEventValidationException;
import com.gegunov.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class OrderEventsListener {

    private final OrderEventValidator validator;
    private final OrderEventRepository orderEventRepository;

    @KafkaListener(topics = { "${order.events.topic}" }, groupId = "billing-service-consumer")
    public void listen(OrderEvent orderEvent) {
        try {
            log.info("Received order event: {}", orderEvent);
            validator.validate(orderEvent);
            OrderEventMapper mapper = Mappers.getMapper(OrderEventMapper.class);
            com.gegunov.billing.jpa.model.OrderEvent order = mapper.toEntity(orderEvent);
            order.setOrderEventState(OrderEventState.RECEIVED);
            order.setId(UUID.randomUUID());
            orderEventRepository.save(order);
            log.info("Successfully processed order event: {}", orderEvent);
        } catch (OrderEventValidationException e) {
            log.error(e);
        }
    }

}
