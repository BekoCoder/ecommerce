package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.ProductEntity;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<ProductEntity> getAllProducts();

    ProductEntity createProduct(ProductDto product) throws IOException;

    void updateProduct(ProductDto product);

    void deletebyId(Long id);

    ProductEntity getProductById(Long id);
}
