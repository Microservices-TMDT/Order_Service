package com.example.order_service.clients;

import com.example.order_service.dto.client.ProductFilter;
import com.example.order_service.dto.response.ProductResponse;

import java.util.List;

public interface ProductClient {
    List<ProductResponse> getProductsByIds(ProductFilter productFilter);
}
