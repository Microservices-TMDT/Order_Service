package com.example.order_service.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    private String userId;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    private OrderAddressRequest address;

    private List<OrderItemRequest> items;
}
