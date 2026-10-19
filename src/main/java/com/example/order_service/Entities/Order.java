package com.example.order_service.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "user_id")
    private String userId;

    @Builder.Default
    @Column(name = "status")
    private String status = "PENDING_PAYMENT";

    @Builder.Default
    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}
