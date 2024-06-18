package com.binar.batch7.CH5.service.impl;

import com.binar.batch7.CH5.dto.OrderDetailRequest;
import com.binar.batch7.CH5.dto.OrderDetailResponse;
import com.binar.batch7.CH5.dto.OrderRequest;
import com.binar.batch7.CH5.dto.OrderResponse;
import com.binar.batch7.CH5.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import com.binar.batch7.CH5.entity.Order;
import com.binar.batch7.CH5.entity.OrderDetail;
import com.binar.batch7.CH5.entity.Product;
import com.binar.batch7.CH5.entity.User;
import com.binar.batch7.CH5.repository.OrderDetailRepo;
import com.binar.batch7.CH5.repository.OrderRepo;
import com.binar.batch7.CH5.repository.ProductRepo;
import com.binar.batch7.CH5.repository.UserRepo;
import com.binar.batch7.CH5.service.OrderService;
import com.binar.batch7.CH5.service.ValidasiService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class OrderImplService implements OrderService {

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private OrderDetailRepo orderDetailRepository;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private ValidasiService validationService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponse create(OrderRequest request) {
        validationService.validate(request);
        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        order.setIsComplete(request.getIsComplete());
        order.setDestinationAddress(request.getDestinationAddress());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + request.getUserId()));
        order.setUser(user);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> findAll() {
        var response = new ArrayList<OrderResponse>();
        orderRepository.findAll().forEach(order -> {
            log.info("ORDER {}", order);
            response.add(orderMapper.toOrderResponse(order));
        });
        return response;
    }

    @Override
    public OrderResponse update(UUID id, OrderRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Order not found"));

        if (Objects.nonNull(request.getDestinationAddress())) {
            order.setDestinationAddress(request.getDestinationAddress());
        }

        if (Objects.nonNull(request.getIsComplete())) {
            order.setIsComplete(request.getIsComplete());
        }

        order.setUser(order.getUser());

        orderRepository.save(order);
        log.info("REQUEST : {}", orderRepository.save(order));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse delete(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Order not found"));
        orderRepository.delete(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderDetailResponse> findAllDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable).map(orderDetail -> orderMapper.toOrderDetailResponse(orderDetail));
    }

    @Override
    public OrderDetailResponse createDetail(OrderDetailRequest request) {
        validationService.validate(request);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(request.getQuantity());
        orderDetail.setTotalPrice(request.getTotalPrice());

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id " + request.getOrderId()));
        orderDetail.setOrder(order);

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id " + request.getProductId()));
        orderDetail.setProduct(product);

        orderDetailRepository.save(orderDetail);

        return orderMapper.toOrderDetailResponse(orderDetail);
    }
}
