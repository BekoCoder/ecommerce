package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrdersEntity;
import com.example.ecommerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {
    List<OrdersEntity> findAllByUserId(UserEntity user);
}
