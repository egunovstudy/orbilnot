package com.gegunov.billing.mapping;

import com.gegunov.billing.jpa.model.OrderEvent;
import org.mapstruct.Mapper;

@Mapper
public interface OrderEventMapper {

    OrderEvent toEntity(com.gegunov.model.OrderEvent orderEvent);

}
