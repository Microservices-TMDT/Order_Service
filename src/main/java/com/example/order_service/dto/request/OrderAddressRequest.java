package com.example.order_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAddressRequest {

    private String recipientName;

    private String recipientPhone;

    private String detailAddress;
}
