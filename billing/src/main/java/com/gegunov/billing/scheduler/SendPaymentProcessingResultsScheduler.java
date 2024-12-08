package com.gegunov.billing.scheduler;

import com.gegunov.billing.jpa.model.OrderEvent;
import com.gegunov.billing.jpa.model.OrderEventState;
import com.gegunov.billing.mapping.PaymentEventMapper;
import com.gegunov.model.PaymentEvent;
import com.gegunov.billing.messages.sender.PaymentEventSender;
import com.gegunov.billing.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class SendPaymentProcessingResultsScheduler {

    private final PaymentEventSender messageSender;
    private final AccountService accountService;

    @Scheduled(fixedDelay = 1000)
    public void sendExecutedEvents() {
        log.debug("Sending payment confirmed events");
        List<OrderEvent> confirmedOrderEvents = accountService.getPaymentConfirmedOrderEvents();
        log.debug("Found {} payment confirmed order events", confirmedOrderEvents.size());
        for (OrderEvent orderEvent : confirmedOrderEvents) {
            log.debug("Processing payment confirmed order event: {}", orderEvent);
            PaymentEventMapper mapper = Mappers.getMapper(PaymentEventMapper.class);
            PaymentEvent paymentEvent = mapper.fromOrderEvent(orderEvent);
            messageSender.sendPaymentEvent(paymentEvent);
            orderEvent.setOrderEventState(OrderEventState.EXECUTED);
            accountService.updateOrderEvent(orderEvent);
            log.debug("Processed payment confirmed order event: {}", orderEvent);
        }
        log.debug("Finished sending payment confirmed events");
    }

    @Scheduled(fixedDelay = 1000)
    public void sendFailedEvents() {
        log.debug("Sending failed events");
        List<OrderEvent> failedOrderEvents = accountService.getPaymentFailedOrderEvents();
        log.debug("Found {} failed order events", failedOrderEvents.size());
        for (OrderEvent orderEvent : failedOrderEvents) {
            log.debug("Processing failed order event: {}", orderEvent);
            PaymentEventMapper mapper = Mappers.getMapper(PaymentEventMapper.class);
            PaymentEvent paymentEvent = mapper.fromOrderEvent(orderEvent);
            messageSender.sendPaymentEvent(paymentEvent);
            orderEvent.setOrderEventState(OrderEventState.EXECUTED);
            accountService.updateOrderEvent(orderEvent);
            log.debug("Processed failed order event: {}", orderEvent);
        }
        log.debug("Finished sending failed events");
    }

}
