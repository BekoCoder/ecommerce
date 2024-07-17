package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Component
@Slf4j
public class AdminController {
    private final ProductService productService;

    @Operation(summary = "Mahsulot qo'shish")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDto product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @Operation(summary = "Barcha mahsulotlarni ko'rish")
    @GetMapping("/allProduct")
    public List<ProductEntity> getAllProduct() {
        log.trace("getAllProduct");
        return ResponseEntity.ok(productService.getAllProducts()).getBody();
    }

    @Operation(summary = "Id orqali mahsulot olish")
    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> productById(@PathVariable (value = "id") Long id) {
        ProductDto dto = productService.getProductById(id);
        log.trace("Accessing GET api/product/get/{}", id);
        return ResponseEntity.ok(dto);
    }



}
