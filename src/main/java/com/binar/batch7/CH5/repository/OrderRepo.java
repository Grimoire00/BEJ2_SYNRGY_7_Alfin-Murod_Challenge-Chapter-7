package com.binar.batch7.CH5.repository;

import com.binar.batch7.CH5.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {
}