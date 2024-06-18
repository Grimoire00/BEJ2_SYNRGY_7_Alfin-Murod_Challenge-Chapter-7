package com.binar.batch7.CH5.mapper;

import com.binar.batch7.CH5.dto.ProductResponse;
import com.binar.batch7.CH5.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
