package com.example.ecommerce.service.product;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.enums.ProductEnum;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity createProduct(ProductDto product) {
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        productEntity.setProductEnumList(List.of(ProductEnum.ARTEL));
        return productRepository.save(productEntity);

    }

    @Override
    public void updateProduct(ProductEntity product) {

    }

    @Override
    public void deletebyId(Long id) {
        productRepository.deleteById(id);
    }
}
