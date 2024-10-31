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
        log.info("Sending payment confirmed events");
        List<OrderEvent> confirmedOrderEvents = accountService.getPaymentConfirmedOrderEvents();
        log.info("Found {} payment confirmed order events", confirmedOrderEvents.size());
        for (OrderEvent orderEvent : confirmedOrderEvents) {
            log.info("Processing payment confirmed order event: {}", orderEvent);
            PaymentEventMapper mapper = Mappers.getMapper(PaymentEventMapper.class);
            PaymentEvent paymentEvent = mapper.fromOrderEvent(orderEvent);
            messageSender.sendPaymentEvent(paymentEvent);
            orderEvent.setOrderEventState(OrderEventState.EXECUTED);
            accountService.updateOrderEvent(orderEvent);
            log.info("Processed payment confirmed order event: {}", orderEvent);
        }
        log.info("Finished sending payment confirmed events");
    }

    @Scheduled(fixedDelay = 1000)
    public void sendFailedEvents() {
        log.info("Sending failed events");
        List<OrderEvent> failedOrderEvents = accountService.getPaymentFailedOrderEvents();
        log.info("Found {} failed order events", failedOrderEvents.size());
        for (OrderEvent orderEvent : failedOrderEvents) {
            log.info("Processing failed order event: {}", orderEvent);
            PaymentEventMapper mapper = Mappers.getMapper(PaymentEventMapper.class);
            PaymentEvent paymentEvent = mapper.fromOrderEvent(orderEvent);
            messageSender.sendPaymentEvent(paymentEvent);
            orderEvent.setOrderEventState(OrderEventState.EXECUTED);
            accountService.updateOrderEvent(orderEvent);
            log.info("Processed failed order event: {}", orderEvent);
        }
        log.info("Finished sending failed events");
    }

}
