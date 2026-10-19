package com.example.order_service.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity {

    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "variant_id")
    private String variantId;

    @Column(name = "quantity")
    private int quantity;
}
