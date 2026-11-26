package com.example.order_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotEmpty(message = "CustomerId không đuợc trống")
    private String userId;

    @NotEmpty(message = "Đơn hàng phải co ít nhất 1 sản phẩm")
    private List<OrderItemRequest> orderItems;

    private OrderAddressRequest address;

}
