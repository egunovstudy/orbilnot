package com.gegunov.kitchen.mapping;

import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.kitchen.jpa.model.OrderItem;
import com.gegunov.model.KitchenOrderEvent;
import com.gegunov.model.KitchenStatusEvent;
import com.gegunov.model.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper
public interface KitchenOrderMapper {

    @Mapping(source = "orderItems", target = "orderItems")
    KitchenOrder toKitchenOrder(KitchenOrderEvent event);

    default OrderItem toOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setDishName(orderItemDTO.getName());
        orderItem.setDetails(orderItemDTO.getIngredients()
                .stream()
                .map(ingredientDTO -> ingredientDTO.getProduct().getCode() + " x" + ingredientDTO.getRequiredAmount())
                .collect(Collectors.joining(",")));
        return orderItem;
    }

    KitchenStatusEvent toKitchenStatusEvent(KitchenOrder event);

}
