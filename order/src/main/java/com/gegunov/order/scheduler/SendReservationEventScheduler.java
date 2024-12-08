package com.gegunov.order.scheduler;

import com.gegunov.model.ReservationEvent;
import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.order.jpa.repository.OrderRepository;
import com.gegunov.order.mapping.OrderMapper;
import com.gegunov.order.messages.sender.ReservationEventSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendReservationEventScheduler {

    private final OrderRepository orderRepository;
    private final ReservationEventSender reservationEventSender;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void processNewOrders() {
        List<Order> newOrders = orderRepository.findByOrderStatus(OrderStatus.NEW);
        for (Order order : newOrders) {
            sendReservationEvent(order);
        }
    }

    private void sendReservationEvent(Order order) {
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        ReservationEvent reservationEvent = mapper.toReservationEvent(order);
        reservationEventSender.sendReservationEvent(reservationEvent);
        order.setOrderStatus(OrderStatus.RESERVATION_SENT);
        orderRepository.save(order);
    }

}
