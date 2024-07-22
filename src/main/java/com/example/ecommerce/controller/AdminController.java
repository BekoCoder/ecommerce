package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.service.product.ProductService;
import com.example.ecommerce.service.user.UserServiceImpl;
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
    private final UserServiceImpl userService;

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

    @Operation(summary = "Id orqali mahsulotni o'chirish")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable (value = "id") Long id) {
        productService.deletebyId(id);
        log.trace("Accessing DELETE /api/product/delete/{}", id);
        return ResponseEntity.ok(Boolean.TRUE);

    }

    @Operation(summary = "Mahsulotni yangilash")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ProductDto product) {
        log.trace("Accessing PUT /api/product/update/{}", product.getId());
        productService.updateProduct(product);
        return ResponseEntity.ok(Boolean.TRUE);

    }

    @Operation(summary = "Userlarni o'chirish id orqali")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        log.trace("Accessing DELETE /api/user/delete/{}", id);
        userService.deleteById(id);
        return ResponseEntity.ok(Boolean.TRUE);
    }



}
