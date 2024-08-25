package com.example.ecommerce.service;

import com.example.ecommerce.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    ImageEntity saveImage(MultipartFile file, ImageEntity imageEntity) throws IOException;

    ImageEntity getImageById(Long id);

    void deleteImageById(Long id);
}
