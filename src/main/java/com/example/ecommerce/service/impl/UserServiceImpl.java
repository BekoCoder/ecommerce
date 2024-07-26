package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.AuthenticationRequest;
import com.example.ecommerce.dto.AuthenticationResponse;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.entity.enums.UserRole;
import com.example.ecommerce.exception.AlreadyExistException;
import com.example.ecommerce.exception.UserNotFoundException;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(List.of(UserRole.USER));
        if (!checkUser(userEntity.getUsername())) {
            userRepository.save(userEntity);
            String jwtToken = jwtService.generateToken(userEntity);
            return AuthenticationResponse.builder()
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

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
