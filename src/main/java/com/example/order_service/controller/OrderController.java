package com.example.order_service.controller;

import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.response.ApiResponse;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .data(orderService.createOrder(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
