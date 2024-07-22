package com.example.ecommerce.service.product;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.enums.ProductEnum;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        if (product.getProductEnumList() == null || product.getProductEnumList().isEmpty()) {
            product.setProductEnumList(List.of(ProductEnum.ARTEL));
        } else {
            if (product.getProductEnumList().size() == 1) {
                ProductEnum singleProductEnum = product.getProductEnumList().get(0);
                if (Arrays.asList(ProductEnum.values()).contains(singleProductEnum)) {
                    product.setProductEnumList(List.of(singleProductEnum));
                } else {
                    throw new ProductNotFoundException("Bunday turdagi mahsulot yo'q");
                }
            } else {
                throw new ProductNotFoundException("Bittadan ortiq mahsulot turini kirita olmaysiz");
            }
        }

        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
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
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<ProductEntity> byId = productRepository.findById(id);
        if(byId.isEmpty()){
            throw  new DataNotFoundException("Mahsulot topilmadi");
        }
        return modelMapper.map(byId.get(), ProductDto.class);
    }
}
