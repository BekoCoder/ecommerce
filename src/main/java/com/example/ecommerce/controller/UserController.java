package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CurrencyDto;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.OrderDetailsEntity;
import com.example.ecommerce.service.CurrencyService;
import com.example.ecommerce.service.impl.UserServiceImpl;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "user-controller", description = "  Userlar bo'yicha xizmatlar")
public class UserController {
    private final UserServiceImpl userService;
    private final CurrencyService currencyService;

    @Operation(summary = "Userni yangilash")
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user, @RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(userService.updateUser(user, userId));
    }

    @Operation(summary = "Mahsulotni Bucketga qo'shish")
    @PostMapping("/add-Bucket")
    public ResponseEntity<String> addToBucket(@RequestParam Long userId, @RequestParam Long productId, int quantity) {
        try {
            userService.addProductToBucket(userId, productId, quantity);
            return ResponseEntity.ok("Mahsulot Savatga qo'shildi");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Sotib olish")
    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestParam Long userId) {
        try {
            userService.checkOut(userId);
            return ResponseEntity.ok("Muvaffaqiyatli sotib olindi");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @Operation(summary = "User o'zi sotib olgan mahsulotlarni ko'rish")
    @GetMapping("/{userId}/purchases")
    public ResponseEntity<List<OrderDetailsEntity>> purchaseUsers(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserPurchases(userId));

    }

    @Operation(summary = "Sotib olgan mahsulotlarining QrCode")
    @GetMapping("/{userId}/buy/qrcode")
    public ResponseEntity<byte[]> buyQRCode(@PathVariable Long userId) throws IOException, WriterException {
        return userService.getUserBuyProductWithQrCode(userId);
    }


}
