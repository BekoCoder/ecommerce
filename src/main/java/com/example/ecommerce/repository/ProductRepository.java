package com.example.ecommerce.repository;

import com.example.ecommerce.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ProductRepository  extends JpaRepository<ProductEntity, Long> {

}