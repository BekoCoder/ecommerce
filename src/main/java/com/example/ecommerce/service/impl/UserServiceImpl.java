package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.AuthenticationRequest;
import com.example.ecommerce.dto.AuthenticationResponse;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.OrderDetailsEntity;
import com.example.ecommerce.entity.OrdersEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.entity.enums.OrderStatus;
import com.example.ecommerce.entity.enums.UserRole;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.jwt_utils.JwtService;
import com.example.ecommerce.qrcode.QrCode;
import com.example.ecommerce.repository.OrdersRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(List.of(UserRole.USER));
        if (!checkUser(userEntity.getUsername())) {
            userRepository.save(userEntity);
            String jwtToken = jwtService.generateToken(userEntity);
            return AuthenticationResponse.builder().username(userEntity.getUsername()).password(userEntity.getPassword()).token(jwtToken).build();
        }
        throw new CustomException("User allaqachon mavjud!!!");
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new CustomException("Parol yoki username xato kiritildi!!!");
        }
        UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
        if (userEntity.getIsDeleted() == 1) {
            throw new CustomException("User o'chirilgan!!!");
        } else {
            String jwtToken = jwtService.generateToken(userEntity);
            return AuthenticationResponse.builder().username(userEntity.getUsername()).password(userEntity.getPassword()).token(jwtToken).build();
        }
    }

    public boolean checkUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void deleteById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new CustomException("User o'chirilgan"));
        userEntity.setIsDeleted(1);
        userRepository.save(userEntity);
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        UserEntity userEntity1 = userRepository.findById(id).orElseThrow(() -> new CustomException("User o'chirilgan"));
        userEntity1.setUsername(userDto.getUsername());
        userEntity1.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity1);
        return modelMapper.map(userEntity1, UserDto.class);
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
        throw new CustomException("User o'chirilgan");
    }

    public void addProductToBucket(Long userId, Long productId, int quantity) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException("User o'chirilgan"));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new CustomException("Product o'chirilgan"));
        OrdersEntity order = ordersRepository.findByUserIdAndStatus(userEntity, OrderStatus.BUCKET).orElse(new OrdersEntity());
        if (userEntity.getIsDeleted() == 1) {
            throw new CustomException("User o'chirilgan");
        }
        if (order.getId() == null) {
            order.setUserId(userEntity);
            order.setStatus(OrderStatus.BUCKET);
        }
        if (product.getQuantity() < quantity) {
            throw new CustomException("Mahsulot miqdori yetmaydi");
        }
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setOrders(order);
        orderDetails.setProduct(product);
        orderDetails.setQuantity((double) quantity);
        order.getOrderDetails().add(orderDetails);
        order.setSum((double) (quantity * product.getPrice()));
        ordersRepository.save(order);
    }

    public void checkOut(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new CustomException("User o'chirilgan"));
        OrdersEntity orders = ordersRepository.findByUserIdAndStatus(userEntity, OrderStatus.BUCKET).orElseThrow(() -> new CustomException("Buket bo'sh"));
        orders.setStatus(OrderStatus.ORDER);
//        if(userEntity.getIsDeleted()==1){
//            throw  new CustomException("User o'chirilgan");
//        }
        for (OrderDetailsEntity orderDetails : orders.getOrderDetails()) {
            ProductEntity product = orderDetails.getProduct();
            double amount = product.getQuantity() - orderDetails.getQuantity();
            product.setQuantity((int) amount);
            productRepository.save(product);
        }
        ordersRepository.save(orders);
    }

    public List<OrderDetailsEntity> getUserPurchases(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Foydalanuvchi topilmadi"));

        List<OrdersEntity> orders = ordersRepository.findAllByUserIdAndStatusNot(user, OrderStatus.BUCKET);

        if (orders.isEmpty()) {
            throw new CustomException("Ma'lumot topilmadi");
        }
        if (user.getIsDeleted() == 1) {
            throw new CustomException("Foydalanuvchi topilmadi");
        }

        List<OrderDetailsEntity> orderDetails = new ArrayList<>();

        for (OrdersEntity order : orders) {
            orderDetails.addAll(order.getOrderDetails());
        }

        return orderDetails;
    }

    public ResponseEntity<byte[]> getUserBuyProductWithQrCode(Long userId) throws IOException, WriterException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Ma'lumot topilmadi"));
        if (userEntity.getIsDeleted() == 1) {
            throw new CustomException("Foydalanuvchi o'chirilgan");
        }
        List<OrdersEntity> orders = ordersRepository.findAllByUserIdAndStatusNot(userEntity, OrderStatus.BUCKET);
        String buyProduct = orders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .map(orderDetails -> "Product: " + orderDetails.getProduct().getName()
                        + " \nQuantity: " + orderDetails.getQuantity()
                        + " \nSum: " + orderDetails.getProduct().getPrice() * orderDetails.getQuantity())
                .collect(Collectors.joining("\n"));
        if (buyProduct.isEmpty()) {
            throw new CustomException("Buyurtmalar mavjud emas");
        }
        byte[] bytes = QrCode.generateQrCodeImages(buyProduct, 300, 300);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "image/png");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


}
