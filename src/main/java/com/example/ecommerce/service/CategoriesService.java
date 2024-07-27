package com.example.ecommerce.service;

import com.example.ecommerce.entity.CategoriesEntity;

import java.util.List;

public interface CategoriesService {
    CategoriesEntity addCategory(CategoriesEntity category);
    CategoriesEntity updateCategory(String categoryName, Long id);
    void deleteCategory(Long id);
    CategoriesEntity getCategoryById(Long id);
    List<CategoriesEntity> getAllCategories();
}
