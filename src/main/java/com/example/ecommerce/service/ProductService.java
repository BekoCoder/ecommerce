package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    List<ProductEntity> getAllProducts();

    ProductEntity createProduct(ProductDto product);

    void updateProduct(ProductDto product);

    void deletebyId(Long id);
    ProductDto getProductById(Long id);
}
