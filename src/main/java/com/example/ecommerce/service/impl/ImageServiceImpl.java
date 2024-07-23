package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.ImageEntity;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.ImageNotFoundException;
import com.example.ecommerce.repository.ImageRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    @Override
    public ImageEntity saveImage(MultipartFile file, ImageEntity imageEntity) throws IOException {
        String filename = file.getOriginalFilename();
        imageEntity.setFileName(filename);
        imageEntity.setSize(file.getSize());
        imageEntity.setData(file.getBytes());
        imageEntity.setProduct(null);
        return imageRepository.save(imageEntity);

    }

    @Override
    public ImageEntity getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Rasm topilmadi"));
    }
}
