package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.CategoriesEntity;
import com.example.ecommerce.exception.CategoryException;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.repository.CategoriesRepository;
import com.example.ecommerce.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    @Override
    public CategoriesEntity addCategory(CategoriesEntity category) {
        if(!CheckCategory(category.getName())){
            return categoriesRepository.save(category);
        }
        throw new CategoryException("Categoriya mavjud");
    }

    @Override
    public CategoriesEntity updateCategory(String categoryName, Long id) {
        Optional<CategoriesEntity> byId = categoriesRepository.findById(id);
        if (byId.isPresent()) {
            CategoriesEntity category = byId.get();
            category.setName(categoryName);
            return categoriesRepository.save(category);
        } else {
            throw new CategoryException("Categoriya mavjud emas");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<CategoriesEntity> byId = categoriesRepository.findById(id);
        if(byId.isEmpty()){
            throw new CategoryException("Categoriya mavjud emas");
        }
        categoriesRepository.deleteById(id);
    }

    @Override
    public CategoriesEntity getCategoryById(Long id) {
        Optional<CategoriesEntity> byId = categoriesRepository.findById(id);
        if(byId.isEmpty()){
            throw new CategoryException("Categoriya mavjud emas");
        }
        return byId.get();
    }

    @Override
    public List<CategoriesEntity> getAllCategories() {
        List<CategoriesEntity> all = categoriesRepository.findAll();
        if(all.isEmpty()){
            throw new DataNotFoundException("Ma'lumot topilmadi");
        }
        return all;
    }

    public boolean CheckCategory(String categoryName) {
       return categoriesRepository.findByName(categoryName).isPresent();
    }
}
