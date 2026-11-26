package com.example.order_service.kafka.consumers;

import com.example.order_service.Entities.Order;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductLockedConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "product_locked")
    public void handleProductLockedEvent(String orderId) {
        log.info("Receive product_locked event for order {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus("PREPARED");
        orderRepository.save(order);
    }
}
