package com.gegunov.billing.mapping;

import com.gegunov.model.OrderEvent;
import org.mapstruct.Mapper;

@Mapper
public interface OrderEventMapper {

    com.gegunov.billing.jpa.model.OrderEvent toEntity(OrderEvent orderEvent);

}
