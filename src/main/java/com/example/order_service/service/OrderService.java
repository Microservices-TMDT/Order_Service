package com.example.order_service.service;

import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
}
