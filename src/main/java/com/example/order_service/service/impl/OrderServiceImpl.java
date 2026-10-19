package com.example.order_service.service.impl;

import com.example.order_service.Entities.Order;
import com.example.order_service.Entities.OrderAddress;
import com.example.order_service.Entities.OrderItem;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.response.OrderAddressResponse;
import com.example.order_service.dto.response.OrderItemResponse;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderAddressRepository;
import com.example.order_service.repository.OrderItemRepository;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderAddressRepository orderAddressRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> {
                    BigDecimal lineTotal = item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
                            .subtract(item.getDiscountAmount());
                    return lineTotal;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(request.getDiscountAmount());

        Order order = orderMapper.toOrder(request);
        order.setOrderCode(generateOrderCode());
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING_PAYMENT");

        Order savedOrder = orderRepository.save(order);

        OrderAddress orderAddress = orderMapper.toOrderAddress(request.getAddress());
        orderAddress.setOrderId(savedOrder.getId());

        OrderAddress savedAddress = orderAddressRepository.save(orderAddress);

        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    OrderItem item = orderMapper.toOrderItem(itemRequest);
                    item.setOrderId(savedOrder.getId());
                    item.setLineTotal(
                            itemRequest.getUnitPrice()
                                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
                                    .subtract(itemRequest.getDiscountAmount()));

                    return item;
                })
                .toList();

        List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems);

        List<OrderItemResponse> itemResponses = savedItems.stream()
                .map(orderMapper::toOrderItemResponse)
                .toList();

        OrderAddressResponse addressResponse = orderMapper.toOrderAddressResponse(savedAddress);

        OrderResponse response = orderMapper.toOrderResponse(savedOrder);
        response.setAddress(addressResponse);
        response.setItems(itemResponses);

        return response;
    }

    private String generateOrderCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String shortUuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ORD-" + timestamp + "-" + shortUuid;
    }
}
