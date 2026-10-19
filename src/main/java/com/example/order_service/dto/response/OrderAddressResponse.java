package com.example.order_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderAddressResponse {

    private String id;

    private String recipientName;

    private String recipientPhone;

    private String detailAddress;
}
