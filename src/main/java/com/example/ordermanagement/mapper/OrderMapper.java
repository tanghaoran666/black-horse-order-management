package com.example.ordermanagement.mapper;

import com.example.ordermanagement.domainModel.OrderModel;
import com.example.ordermanagement.dto.OrderCreateRequest;
import com.example.ordermanagement.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);

    OrderModel toOrderModel(OrderCreateRequest orderCreateRequest);

    Order toOrder(OrderModel orderModel);
}
