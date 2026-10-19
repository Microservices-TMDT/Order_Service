package com.example.order_service.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "variant_id")
    private String variantId;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private int quantity;

    @Builder.Default
    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "line_total")
    private BigDecimal lineTotal;
}
