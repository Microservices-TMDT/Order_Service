package com.example.order_service.service.impl;

import com.example.order_service.Entities.Order;
import com.example.order_service.Entities.OrderItem;
import com.example.order_service.clients.ProductClient;
import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.request.OrderItemRequest;
import com.example.order_service.dto.response.OrderItemResponse;
import com.example.order_service.dto.response.OrderResponse;
import com.example.order_service.kafka.events.OrderCreatedEvent;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderItemRepository;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        // List<String> productIds = request.getOrderItems().stream()
        // .map(OrderItemRequest::getProductId)
        // .distinct()
        // .toList();
        //
        // List<ProductResponse> products = productClient.getProductsByIds(new
        // ProductFilter(productIds));
        //
        // Map<String, ProductResponse> productPriceMap = new HashMap<>();
        // products.forEach(productResponse -> {
        // productPriceMap.put(productResponse.getId(), productResponse);
        // });

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("NEW");
        order.setTotalAmount(BigDecimal.ZERO);
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            // ProductResponse productResponse =
            // productPriceMap.get(itemRequest.getProductId());
            // if (productResponse == null) {
            // throw new ApplicationException("Product " + itemRequest.getProductId() + "
            // not existed");
            // }
            // if (itemRequest.getQuantity() > productResponse.getStock()) {
            // throw new ApplicationException("Product " + itemRequest.getProductId() + "
            // not enough");
            // }
            // Integer price = productResponse.getPrice();

            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setVariantId(itemRequest.getProductId());
            item.setUnitPrice(BigDecimal.ZERO);
            item.setQuantity(itemRequest.getQuantity());
            orderItems.add(item);
        }

        orderItemRepository.saveAll(orderItems);
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderRepository.save(savedOrder));
        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(orderMapper::toOrderItemResponse)
                .toList();
        orderResponse.setItems(itemResponses);
        OrderCreatedEvent orderCreatedEvent = orderMapper.toEvent(orderResponse);
        kafkaTemplate.send("order_created", orderCreatedEvent);
        log.info("Publish new order success to order_created");
        return orderResponse;
    }
}
