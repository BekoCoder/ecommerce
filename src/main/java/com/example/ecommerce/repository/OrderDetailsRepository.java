package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderDetailsEntity;
import com.example.ecommerce.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {
    List<OrderDetailsEntity> findAllByOrders(OrdersEntity orders);
}
