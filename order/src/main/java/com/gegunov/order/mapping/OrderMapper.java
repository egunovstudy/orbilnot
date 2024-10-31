package com.gegunov.order.mapping;

import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.OrderStatus;
import com.gegunov.model.OrderEvent;
import com.gegunov.model.OrderEventType;
import com.gegunov.order.web.model.OrderAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

import java.util.List;

@Mapper
public interface OrderMapper {

    Order toOrder(OrderAction dto);

    List<OrderAction> toOrderActionList(List<Order> entity);


    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "orderStatus", target = "eventType")
    OrderEvent toOrderEvent(Order entity);

    @ValueMapping(source = "NEW", target = "CREATED")
    @ValueMapping(source = "SENT", target = "CREATED")
    @ValueMapping(source = "CONFIRMED", target = "CREATED")
    @ValueMapping(source = "EXECUTED", target = "CREATED")
    @ValueMapping(source = "EXPIRED", target = "CANCELLED")
    OrderEventType toOrderEventType(OrderStatus orderStatus);

}
