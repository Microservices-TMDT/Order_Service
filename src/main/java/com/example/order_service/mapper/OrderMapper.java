package com.example.order_service.mapper;

import com.example.order_service.Entities.Order;
import com.example.order_service.Entities.OrderAddress;
import com.example.order_service.Entities.OrderItem;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.request.OrderAddressRequest;
import com.example.order_service.dto.request.OrderItemRequest;
import com.example.order_service.dto.response.OrderAddressResponse;
import com.example.order_service.dto.response.OrderItemResponse;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.kafka.events.OrderCreatedEvent;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderCode", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order toOrder(CreateOrderRequest request);

    OrderItem toOrderItem(OrderItemRequest request);

    OrderAddress toOrderAddress(OrderAddressRequest request);

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productId", source = "variantId")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    OrderAddressResponse toOrderAddressResponse(OrderAddress orderAddress);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    OrderCreatedEvent toEvent(OrderResponse orderResponse);
}
