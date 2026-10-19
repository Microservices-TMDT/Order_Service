package com.example.order_service.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    private String variantId;

    private String variantName;

    private BigDecimal unitPrice;

    private int quantity;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;
}
