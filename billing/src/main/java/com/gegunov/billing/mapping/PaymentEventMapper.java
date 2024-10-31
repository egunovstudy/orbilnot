package com.gegunov.billing.mapping;

import com.gegunov.billing.jpa.model.OrderEvent;
import com.gegunov.billing.jpa.model.OrderEventState;
import com.gegunov.model.PaymentEvent;
import com.gegunov.model.PaymentEventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

@Mapper
public interface PaymentEventMapper {

    @Mapping(source = "orderEventState", target = "paymentEventType")
    @Mapping(source = "errorMessage", target = "description")
    PaymentEvent fromOrderEvent(OrderEvent orderEvent);

    @ValueMapping(source = "PAYMENT_CONFIRMED", target = "PAYMENT_CONFIRMED")
    @ValueMapping(source = "PAYMENT_DENIED", target = "PAYMENT_DENIED")
    @ValueMapping(source = "RECEIVED", target = "NOT_PROCESSED")
    @ValueMapping(source = "EXECUTED", target = "PROCESSED")
    PaymentEventType toPaymentEventType(OrderEventState orderStatus);

}
