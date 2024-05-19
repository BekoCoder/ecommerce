package com.example.ecommerce.service.user;

import com.example.ecommerce.dto.AuthenticationRequest;
import com.example.ecommerce.dto.AuthenticationResponse;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.entity.enums.UserRole;
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
public class UserService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final ModelMapper modelMapper;
        private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(List.of(UserRole.USER));
        userRepository.save(userEntity);
        var jwtToken=jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken=jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
