package com.example.order_service.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart extends BaseEntity {

    @Column(name = "user_id")
    private String userId;

    @Builder.Default
    @Column(name = "status")
    private String status = "ACTIVE";
}
