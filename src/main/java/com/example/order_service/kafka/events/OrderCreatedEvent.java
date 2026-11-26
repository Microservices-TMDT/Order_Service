package com.example.order_service.kafka.events;

import com.example.order_service.dto.response.OrderResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class OrderCreatedEvent extends OrderResponse {

}
