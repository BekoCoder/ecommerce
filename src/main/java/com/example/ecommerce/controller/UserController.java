package com.example.ecommerce.controller;

import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
