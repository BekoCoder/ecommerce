package com.example.ecommerce.dto;

import com.example.ecommerce.entity.enums.UserRole;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    private String username;
    private String password;
    private Integer isDeleted=0;
    private List<UserRole> roles;
}
