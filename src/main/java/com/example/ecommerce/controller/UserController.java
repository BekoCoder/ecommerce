package com.example.ecommerce.controller;

import com.example.ecommerce.entity.OrderDetailsEntity;
import com.example.ecommerce.entity.OrdersEntity;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.service.impl.UserServiceImpl;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @Operation(summary = "Userni yangilash")
    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user, @RequestParam(value = "userId", required = false) Long userId) {
        return ResponseEntity.ok(userService.updateUser(user, userId));
    }

    @Operation(summary = "Mahsulot sotib olish")
    @PostMapping("/{userId}/purchase")
    public ResponseEntity<String> purchaseUser(
            @PathVariable Long userId,
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "quantity") int quantity) {
        userService.buyProduct(userId, productId, quantity);
        return ResponseEntity.ok("Mahsulot muvaffaqiyatli sotib olindi");
    }

    @Operation(summary = "User o'zi sotib olgan mahsulotlarni ko'rish")
    @GetMapping("/{userId}/purchases")
    public ResponseEntity<List<OrdersEntity>> purchaseUsers(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserPurchases(userId));

    }

    @Operation(summary = "Sotib olgan mahsulotlarining QrCode")
    @GetMapping("/{userId}/buy/qrcode")
    public ResponseEntity<byte[]> buyQRCode(@PathVariable Long userId) throws IOException, WriterException {
        return userService.getUserBuyProductWithQrCode(userId);
    }


}
