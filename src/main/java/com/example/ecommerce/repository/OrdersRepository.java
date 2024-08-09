package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrdersEntity;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {
    List<OrdersEntity> findAllByUserId(UserEntity user);

    Optional<OrdersEntity> findByUserIdAndStatus(UserEntity user, OrderStatus status);
}
