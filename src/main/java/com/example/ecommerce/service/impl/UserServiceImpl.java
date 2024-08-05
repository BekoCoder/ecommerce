package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.entity.OrderDetailsEntity;
import com.example.ecommerce.entity.OrdersEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.entity.enums.UserRole;
import com.example.ecommerce.exception.AlreadyExistException;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.ProductNotEnoughException;
import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.jwt_utils.JwtService;
import com.example.ecommerce.qrcode.QrCode;
import com.example.ecommerce.repository.OrderDetailsRepository;
import com.example.ecommerce.repository.OrdersRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.ProductService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final ProductService productService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrdersRepository ordersRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(List.of(UserRole.USER));
        if (!checkUser(userEntity.getUsername())) {
            userRepository.save(userEntity);
            String jwtToken = jwtService.generateToken(userEntity);
            return AuthenticationResponse.builder()
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .token(jwtToken)
                    .build();
        }
        throw new AlreadyExistException("User allaqachon mavjud");
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if (userEntity.getIsDeleted() == 1) {
            throw new UserNotFoundException("User o'chirilgan");
        } else {
            String jwtToken = jwtService.generateToken(userEntity);
            return AuthenticationResponse.builder()
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .token(jwtToken)
                    .build();
        }
    }

    public boolean checkUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void deleteById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User o'chirilgan"));
        userEntity.setIsDeleted(1);
        userRepository.save(userEntity);
    }

    public UserEntity updateUser(UserEntity userEntity, Long id) {
        UserEntity userEntity1 = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User o'chirilgan"));
        userEntity1.setUsername(userEntity.getUsername());
        userEntity1.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity1);
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> all = userRepository.findAll();
        if (all.isEmpty()) {
            throw new DataNotFoundException("Ma'lumot topilmadi");
        }
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity userEntity : all) {
            userDtos.add(modelMapper.map(userEntity, UserDto.class));
        }
        return userDtos;

    }

    public UserDto getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            return modelMapper.map(userEntity, UserDto.class);
        }
        throw new UserNotFoundException("User o'chirilgan");
    }

    public void buyProduct(Long userId, Long productId, int quantity) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User o'chirilgan"));
        ProductEntity product = productService.getProductById(productId);
        if (product.getQuantity() < quantity) {
            throw new ProductNotEnoughException("Mahsulot yetarli emas");
        }
        if (product.getId() == null) {
            throw new DataNotFoundException("Ma'lumot topilmadi");
        }
        OrdersEntity orders = new OrdersEntity();
        orders.setSum((double) (product.getPrice() * quantity));
        orders.setStatus(true);
        orders.setUserId(userEntity);
        ordersRepository.save(orders);

        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setProduct(product);
        orderDetails.setOrders(orders);
        orderDetails.setQuantity((double) quantity);
        orderDetailsRepository.save(orderDetails);

        product.setQuantity(product.getQuantity() - quantity);
        productService.updateProduct(modelMapper.map(product, ProductDto.class));
    }

    public List<OrdersEntity> getUserPurchases(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Foydalanuvchi topilmadi"));
        List<OrdersEntity> allByCreatedBy = ordersRepository.findAllByUserId(user);
        if (allByCreatedBy.isEmpty()) {
            throw new DataNotFoundException("Ma'lumot topilmadi");
        }
        return allByCreatedBy;
    }

    public ResponseEntity<byte[]> getUserBuyProductWithQrCode(Long userId) throws IOException, WriterException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Ma'lumot topilmadi"));
        List<OrdersEntity> orders = ordersRepository.findAllByUserId(userEntity);
        String buyProduct = orders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .map(orderDetails -> "Product: " + orderDetails.getProduct().getName() + " Quantity: " + orderDetails.getQuantity())
                .collect(Collectors.joining("\n"));


        byte[] bytes = QrCode.generateQrCodeImages(buyProduct, 300, 300);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "image/png");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


}
