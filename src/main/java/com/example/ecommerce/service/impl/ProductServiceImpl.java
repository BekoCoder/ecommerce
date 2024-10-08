package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.CategoriesEntity;
import com.example.ecommerce.entity.ImageEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.enums.ProductEnum;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.repository.CategoriesRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        List<ProductEntity> all = productRepository.findAll();
        if (all.isEmpty()) {
            throw new CustomException("Mahsulot topilmadi");
        }
        return all;
    }

    @Override
    public ProductEntity createProduct(ProductDto product) throws IOException {
        if (product.getProductEnumList() == null || product.getProductEnumList().isEmpty()) {
            product.setProductEnumList(List.of(ProductEnum.ARTEL));
        } else {
            if (product.getProductEnumList().size() == 1) {
                ProductEnum singleProductEnum = product.getProductEnumList().get(0);
                if (Arrays.asList(ProductEnum.values()).contains(singleProductEnum)) {
                    product.setProductEnumList(List.of(singleProductEnum));
                } else {
                    throw new CustomException("Bunday turdagi mahsulot yo'q");
                }
            } else {
                throw new CustomException("Bittadan ortiq mahsulot turini kirita olmaysiz");
            }
        }

        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        CategoriesEntity categories = categoriesRepository.findById(product.getCategoryId()).orElseThrow(() -> new CustomException("Kategoriya topilmadi"));
        productEntity.setCategories(categories);
        if (product.getFile() != null && !product.getFile().isEmpty()) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setFileName(product.getFile().getOriginalFilename());
            imageEntity.setSize(product.getFile().getSize());
            imageEntity.setData(product.getFile().getBytes());
            imageEntity.setProduct(productEntity);
            productEntity.setImage(imageEntity);
        }
        return productRepository.save(productEntity);

    }

    @Override
    public void updateProduct(ProductDto product) {
        ProductEntity productEntity = productRepository.findById(product.getId()).orElseThrow(() -> new DataNotFoundException("Product not found"));
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setColor(product.getColor());
        productEntity.setQuantity(product.getQuantity());
        productRepository.save(productEntity);
    }

    @Override
    public void deletebyId(Long id) {
        Optional<ProductEntity> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new CustomException("Mahsulot topilmadi");
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<ProductEntity> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new CustomException("Mahsulot topilmadi");
        }
        return modelMapper.map(byId, ProductDto.class);
    }
}
