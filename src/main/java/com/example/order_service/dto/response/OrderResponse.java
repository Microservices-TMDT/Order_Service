package com.example.order_service.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private String id;

    private String orderCode;

    private String userId;

    private String status;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private OrderAddressResponse address;

    private List<OrderItemResponse> items;
}
