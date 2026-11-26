package com.example.order_service.clients.impl;

import com.example.order_service.clients.ProductClient;
import com.example.order_service.dto.client.ProductFilter;
import com.example.order_service.dto.response.ApiResponse;
import com.example.order_service.dto.response.ProductResponse;
import com.example.order_service.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

//    private final WebClient.Builder webClientBuilder;

    @Override
    public List<ProductResponse> getProductsByIds(ProductFilter productFilter) {
        WebClient.Builder builder = WebClient.builder();
        ApiResponse<List<ProductResponse>> response = builder.build()
                .post()
                .uri("http://product_service/api/v1/products/search")
                .bodyValue(productFilter)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<ProductResponse>>>() {
                })
                .block();
        if (response == null || response.getData() == null) {
            throw new ApplicationException("Không thể lấy thông tin sản phẩm từ Product Service");
        }
        return response.getData();
    }
}
