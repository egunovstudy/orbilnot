package com.gegunov.order.mapping;

import com.gegunov.model.*;
import com.gegunov.order.jpa.model.*;
import com.gegunov.order.web.model.OrderAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderMapper {

    Order toOrder(OrderAction dto);

    List<OrderAction> toOrderActionList(List<Order> entity);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "orderStatus", target = "eventType")
    OrderEvent toOrderEvent(Order entity);

    KitchenOrderEvent toKitchenEvent(Order entity);

    List<OrderItemDTO> toListOrderItemDTO (List<OrderItem> orderItems);

    ProductDTO productToProductStockEventDTO(Product product);

    @Mapping(source = "orderItems", target = "ingredients")
    ReservationEvent toReservationEvent(Order entity);

    IngredientDTO toIngredientDTO(Ingredient ingredient);

    default List<IngredientDTO> orderItemToIngredientDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        List<IngredientDTO> result = new ArrayList<>();
        for (Ingredient ingredient : orderItem.getIngredients()) {
            result.add(toIngredientDTO(ingredient));
        }
        return result;
    }

    default List<IngredientDTO> orderItemListToIngredientDTOList(List<OrderItem> list) {
        if (list == null) {
            return null;
        }

        List<IngredientDTO> list1 = new ArrayList<IngredientDTO>(list.size());
        for (OrderItem orderItem : list) {
            list1.addAll(orderItemToIngredientDTO(orderItem));
        }

        return list1;
    }

    @ValueMapping(source = "NEW", target = "CREATED")
    @ValueMapping(source = "BILLING_SENT", target = "CREATED")
    @ValueMapping(source = "BILLING_CONFIRMED", target = "CREATED")
    @ValueMapping(source = "RESERVATION_SENT", target = "CREATED")
    @ValueMapping(source = "RESERVATION_CONFIRMED", target = "CREATED")
    @ValueMapping(source = "PREPARING", target = "CREATED")
    @ValueMapping(source = "READY", target = "CREATED")
    @ValueMapping(source = "EXECUTED", target = "CREATED")
    @ValueMapping(source = "EXPIRED", target = "CANCELLED")
    OrderEventType toOrderEventType(OrderStatus orderStatus);

}
