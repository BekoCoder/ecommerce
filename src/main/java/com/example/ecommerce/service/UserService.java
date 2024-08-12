package com.example.ecommerce.service;

import com.example.ecommerce.dto.AuthenticationRequest;
import com.example.ecommerce.dto.AuthenticationResponse;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.OrderDetailsEntity;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface UserService {

    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    boolean checkUser(String username);

    void deleteById(Long id);

    UserDto updateUser(UserDto userDto, Long id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void addProductToBucket(Long userId, Long productId, int quantity);

    void checkOut(Long userId);

    List<OrderDetailsEntity> getUserPurchases(Long userId);

    ResponseEntity<byte[]> getUserBuyProductWithQrCode(Long userId) throws IOException, WriterException;

}
