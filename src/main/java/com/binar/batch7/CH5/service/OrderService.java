package com.binar.batch7.CH5.service;

import com.binar.batch7.CH5.dto.OrderDetailRequest;
import com.binar.batch7.CH5.dto.OrderDetailResponse;
import com.binar.batch7.CH5.dto.OrderRequest;
import com.binar.batch7.CH5.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(OrderRequest request);

    List<OrderResponse> findAll();

    OrderResponse update(UUID id, OrderRequest request);

    OrderResponse delete(UUID id);

    Page<OrderDetailResponse> findAllDetails(Pageable pageable);

    OrderDetailResponse createDetail(OrderDetailRequest request);
}
