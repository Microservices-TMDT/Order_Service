package com.example.order_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    @NotEmpty
    private String productId;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private int quantity;
}
