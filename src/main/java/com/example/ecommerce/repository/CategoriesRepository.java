package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {
    Optional<CategoriesEntity> findByName(String name);
}
