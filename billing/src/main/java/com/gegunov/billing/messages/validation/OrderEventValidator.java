package com.gegunov.billing.messages.validation;

import com.gegunov.billing.jpa.repository.OrderEventRepository;
import com.gegunov.billing.messages.validation.exceptions.OrderEventValidationException;
import com.gegunov.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderEventValidator {

    private final OrderEventRepository orderEventRepository;

    public void validate(OrderEvent orderEvent) {
        if (orderEvent == null) {
            throw new OrderEventValidationException("OrderEvent cannot be null");
        }
        if (orderEvent.getEventType() == null) {
            throw new OrderEventValidationException("Event type cannot be null");
        }
        if (orderEvent.getAccountId() == null) {
            throw new OrderEventValidationException("User id cannot be null or empty");
        }
        if (orderEvent.getAmount() == null || isNegative(orderEvent.getAmount())) {
            throw new OrderEventValidationException("Total amount cannot be negative or zero");
        }
        if (StringUtils.isEmpty(orderEvent.getOrderNumber())) {
            throw new OrderEventValidationException("Order number cannot be null or empty");
        }
        if (!orderEventRepository.findByOrderNumber(orderEvent.getOrderNumber()).isEmpty()) {
            throw new OrderEventValidationException("Order with such number already exists");
        }

    }

    private static boolean isNegative(BigDecimal totalAmount) {
        return totalAmount.compareTo(BigDecimal.ZERO) <= 0;
    }

}
