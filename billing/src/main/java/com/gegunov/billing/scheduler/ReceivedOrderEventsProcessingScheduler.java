package com.gegunov.billing.scheduler;

import com.gegunov.billing.jpa.model.OrderEvent;
import com.gegunov.billing.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ReceivedOrderEventsProcessingScheduler {

    private final AccountService accountService;

    @Scheduled(fixedRate = 1000)
    public void processReceivedOrders() {
        log.info("Processing received order events...");
        List<OrderEvent> receivedOrderEvents = accountService.getReceivedOrderEvents();
        log.info("Found {} order events", receivedOrderEvents.size());
        for (OrderEvent orderEvent : receivedOrderEvents) {
            accountService.processOrderEvent(orderEvent);

        }
    }

}
