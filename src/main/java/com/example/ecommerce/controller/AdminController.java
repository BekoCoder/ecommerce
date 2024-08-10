package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CategoriesDto;
import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.CategoriesEntity;
import com.example.ecommerce.entity.ImageEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.exception.CategoryException;
import com.example.ecommerce.service.CategoriesService;
import com.example.ecommerce.service.ImageService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Component
@Slf4j
public class AdminController {
    private final ProductService productService;
    private final UserServiceImpl userService;
    private final ImageService imageService;
    private final CategoriesService categoriesService;

    @Operation(summary = "Mahsulot qo'shish")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute ProductDto product) throws IOException {
        log.trace("Accessing product: {}", product);
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
    public ResponseEntity<ProductEntity> productById(@PathVariable(value = "id") Long id) {
        ProductEntity entity = productService.getProductById(id);
        log.trace("Accessing GET api/product/get/{}", id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Id orqali mahsulotni o'chirish")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable(value = "id") Long id) {
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

    @Operation(summary = "Barcha userlarni olish")
    @GetMapping("/get-user")
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Userni id orqali olish")
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") Long id) {
        log.trace("Accessing GET /api/user/get/{}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Rasm qo'shish")
    @PostMapping("/images")
    public ResponseEntity<String> uploadImage(@RequestParam(value = "file") MultipartFile file, ImageEntity imageEntity) throws IOException {
        imageService.saveImage(file, imageEntity);
        return ResponseEntity.ok("Rasm saqlandi");
    }

    @Operation(summary = "Kategoriya qo'shish")
    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody CategoriesDto categories) {
        log.trace("Accessing POST /api/add-category", categories);
        categoriesService.addCategory(categories);
        return ResponseEntity.ok("Kategoriya saqlandi");
    }

    @Operation(summary = "Id orqali Kategoriyani olish")
    @GetMapping("/get-category/{id}")
    public ResponseEntity<List<CategoriesEntity>> getCategory(@PathVariable Long id) {
        log.trace("Accessing GET /api/get-category/{}", id);
        CategoriesEntity id1 = categoriesService.getCategoryById(id);
        return ResponseEntity.ok(Collections.singletonList(id1));
    }

    @Operation(summary = "Barcha kategoriyalarni olish")
    @GetMapping("/getCategory")
    public ResponseEntity<List<CategoriesEntity>> getAllCategory() {
        log.trace("Accessing GET /api/get-category");
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @Operation(summary = "Kategoriyani o'chirish")
    @DeleteMapping("/delete-category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        log.trace("Accessing DELETE /api/delete-category/{}", id);
        categoriesService.deleteCategory(id);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(summary = "Kategoriyani  yangilash")
    @PutMapping("/update-category/{id}")
    public ResponseEntity<CategoriesEntity> updateCategory(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            CategoriesEntity updatedCategory = categoriesService.updateCategory(name, id);
            return ResponseEntity.ok(updatedCategory);
        } catch (CategoryException e) {
            return ResponseEntity.status(404).body(null);
        }
    }


}
